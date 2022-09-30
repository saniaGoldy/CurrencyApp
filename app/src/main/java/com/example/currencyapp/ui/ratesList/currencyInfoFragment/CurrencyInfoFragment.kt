package com.example.currencyapp.ui.ratesList.currencyInfoFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.currencyapp.R
import com.example.currencyapp.databinding.FragmentCurrencyInfoBinding
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.DataState.Failure
import com.example.currencyapp.domain.model.DataState.Success
import com.google.accompanist.appcompattheme.AppCompatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyInfoFragment : Fragment() {

    private val viewModel: CurrencyInfoViewModel by activityViewModels()

    private var _binding: FragmentCurrencyInfoBinding? = null
    private val binding get() = _binding!!

    private val args: CurrencyInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyInfoBinding.inflate(inflater, container, false)

        viewModel.loadCurrencyData(args.currencyCode)

        setupTVs()
        setupObservers()

        return binding.root
    }

    private fun setupTVs() = with(binding) {
        tvChartFragmentTitle.text = viewModel.getTvChartFragmentTitle(args.currencyCode)
        tvChartFragmentDetails.text = viewModel.getTvChartFragmentDetails(args.settings.currencyCode)
    }

    private fun setupObservers() {
        viewModel.currency.observe(viewLifecycleOwner) { dataState ->
            binding.progressBarRatesChart.isVisible = dataState is DataState.Loading
            when (dataState) {
                is Success -> {
                    val currencyRateStory = dataState.result.rateStory ?: mapOf()

                    binding.tvChartTimeStamp.text =
                        viewModel.getFromToLabel(currencyRateStory.keys.toList())

                    binding.rateStoryChart.setContent {
                        ShowRateStoryChart(currencyRateStory)
                    }
                }
                is Failure -> {
                    showErrorMessageToast()
                }
                else -> {}
            }
        }
    }

    @Composable
    private fun ShowRateStoryChart(currencyRateStory: Map<String, Double>) {
        AppCompatTheme {
            CurrencyRatesChart(
                rateStory = currencyRateStory,
                modifier = Modifier
            )
        }
    }

    private fun showErrorMessageToast() {
        Toast.makeText(
            requireContext(),
            getString(R.string.standard_error_message),
            Toast.LENGTH_SHORT
        ).show()
    }

}