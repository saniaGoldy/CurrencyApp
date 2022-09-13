package com.example.currencyapp.ui.ratesList.currencyInfoFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.currencyapp.databinding.FragmentCurrencyInfoBinding
import com.example.currencyapp.domain.repository.MainRepository.DataState.Success
import com.example.currencyapp.ui.ratesList.RatesListViewModel
import com.google.accompanist.appcompattheme.AppCompatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrencyInfoFragment : Fragment() {

    private val viewModel: RatesListViewModel by activityViewModels()

    private var _binding: FragmentCurrencyInfoBinding? = null
    private val binding get() = _binding!!

    private val args: CurrencyInfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyInfoBinding.inflate(inflater, container, false)
        val rateStory = viewModel.ratesDataState.value.let { dataState ->
            if (dataState is Success)
                dataState.result.find { it.iso4217Alpha == args.currencyCode }?.rateStory ?: mapOf()
            else mapOf()
        }

        binding.rateStoryChart.setContent { 
            AppCompatTheme {
                CurrencyRatesChart(rateStory = rateStory,modifier = Modifier)
            }
        }
        return binding.root
    }

}