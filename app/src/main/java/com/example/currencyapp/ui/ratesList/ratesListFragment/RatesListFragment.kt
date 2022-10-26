package com.example.currencyapp.ui.ratesList.ratesListFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.currencyapp.R
import com.example.currencyapp.TAG
import com.example.currencyapp.databinding.FragmentRatesListBinding
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.DataState.*
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.model.rates.RatesListSettings
import com.example.currencyapp.domain.services.ConnectivityObserver
import com.example.currencyapp.ui.ratesList.ratesListSettingsDialog.RatesSettingsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatesListFragment : Fragment() {

    private lateinit var currenciesListAdapter: CurrenciesListAdapter

    private val viewModel: RatesListViewModel by viewModels()

    private var _binding: FragmentRatesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatesListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCurrenciesList()
        setupButtons()
        setupObservers()
    }

    private fun setupButtons() {
        with(binding) {

            ratesSettingsImageButton.setOnClickListener {
                viewModel.ratesSettings.value.let { ratesListSettings ->
                    RatesSettingsDialog.newInstance(
                        ratesListSettings
                    ).show(childFragmentManager, RatesSettingsDialog.TAG)
                }
            }
        }
    }

    private fun setupSearchView(cashedListState: DataState<List<CurrencyData>>?) {
        binding.searchView.setOnQueryTextListener(
            currenciesListAdapter.getOnQueryTextListener(
                cashedListState
            )
        )
    }

    private fun setupObservers() {
        setupRatesSettingsObserver()
        setupRatesObserver()
    }

    private fun setupRatesObserver() {
        viewModel.ratesDataState.observe(viewLifecycleOwner) { dataState ->
            binding.progressBar.isVisible = dataState is Loading
            when (dataState) {
                is Success -> {
                    currenciesListAdapter.currenciesList = dataState.result
                    setupSearchView(dataState)

                    dataState.info?.let { showToast(it) }
                }
                is Failure -> {
                    Log.e(TAG, "dataStateObserver Failure: ${dataState.errorInfo}")

                    showToast()
                }
                else -> {}
            }
        }
    }

    private fun setupRatesSettingsObserver() {
        viewModel.ratesSettings.observe(viewLifecycleOwner) { settings ->
            binding.tvRatesTitle.text =
                getString(R.string.currency_rates_title, settings.currencyCode)
            currenciesListAdapter.setRoundingFormat(settings.precision)

            //load rates after settings loaded
            viewModel.updateDataState()
        }
    }

    /** Pass null to [message] to use standard message*/
    private fun showToast(message: String? = null) {
        Toast.makeText(
            this.requireContext(),
            if (viewModel.networkStatus.value == ConnectivityObserver.Status.Available) {
                message ?: getString(R.string.standard_error_message)
            } else {
                getString(R.string.no_internet_connection_error_message)
            },
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setupCurrenciesList() {
        currenciesListAdapter =
            CurrenciesListAdapter(
                object : CurrenciesListAdapter.ItemClickedAction {
                    override fun run(currencyData: CurrencyData) {
                        binding.root.findNavController().navigate(
                            RatesListFragmentDirections.actionNavigationCurrenciesToCurrencyInfoFragment(
                                currencyData.currency.name,
                                viewModel.ratesSettings.value ?: RatesListSettings()
                            )
                        )
                    }
                }
            )

        binding.rvCurrenciesList.adapter = currenciesListAdapter
    }

}
