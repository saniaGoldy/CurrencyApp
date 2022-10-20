package com.example.currencyapp.ui.ratesList.currencyInfoFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.currencyapp.ui.model.CurrencyInfoResourceProvider
import com.google.accompanist.appcompattheme.AppCompatTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyInfoFragment : Fragment() {

    private val viewModel: CurrencyInfoViewModel by activityViewModels()

    @Inject
    lateinit var resourceProvider: CurrencyInfoResourceProvider
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
        tvChartFragmentTitle.text = resourceProvider.getTvChartFragmentTitle(args.currencyCode)
        tvChartFragmentDetails.text =
            resourceProvider.getTvChartFragmentDetails(args.settings.currencyCode)
    }

    private fun setupObservers() {
        viewModel.currency.observe(viewLifecycleOwner) { dataState ->
            binding.progressBarRatesChart.isVisible = dataState is DataState.Loading
            when (dataState) {
                is Success -> {
                    val currencyRateStory = dataState.result.rateStory ?: mapOf()

                    binding.tvChartTimeStamp.text =
                        resourceProvider.getFromToLabel(currencyRateStory.keys.toList())

                    showRateStoryChart(currencyRateStory = currencyRateStory)
                }
                is Failure -> {
                    showErrorMessageToast()
                }
                else -> {}
            }
        }
    }

    private fun showRateStoryChart(currencyRateStory: Map<String, Double>) {
        binding.rateStoryChart.setContent {
            AppCompatTheme {
                CurrencyRatesChart(
                    rateStory = currencyRateStory,
                    modifier = Modifier
                )
            }
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