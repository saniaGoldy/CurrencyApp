package com.example.currencyapp.ui.ratesList.homeFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.currencyapp.R
import com.example.currencyapp.TAG
import com.example.currencyapp.databinding.FragmentHomeBinding
import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.repository.MainRepository.DataState.*
import com.example.currencyapp.domain.services.ConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var currenciesListAdapter: CurrenciesListAdapter

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCurrenciesList()

        with(binding.searchView) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filter(newText)
                    return true
                }

            })
        }

        setupObservers()
    }

    fun filter(keyword: String?) {
        val cashedListState = viewModel.ratesDataState.value

        if (cashedListState is Success) {
            currenciesListAdapter.currenciesList = if (!keyword.isNullOrEmpty())
                cashedListState.result.filter {
                    it.iso4217Alpha.contains(keyword, true) || it.fullName.contains(keyword, true)
                }
            else
                cashedListState.result
        }
    }

    private fun setupObservers() {

        viewModel.ratesDataState.observe(viewLifecycleOwner) { dataState ->
            binding.progressBar.isVisible = dataState is Loading
            when (dataState) {
                is Success -> {
                    currenciesListAdapter.currenciesList = dataState.result
                }
                is Failure -> {
                    Log.e(TAG, "dataStateObserver Failure: ${dataState.errorInfo}")

                    Toast.makeText(
                        this.requireContext(),
                        if (viewModel.networkStatus.value == ConnectivityObserver.Status.Available) getString(
                            R.string.standart_error_message
                        ) else getString(
                            R.string.no_internet_connection_error_message
                        ),
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {}
            }
        }

    }

    private fun setupCurrenciesList() {
        currenciesListAdapter =
            CurrenciesListAdapter(
                object : CurrenciesListAdapter.ItemClickedAction {
                    override fun run(currencyData: CurrencyData) {
                        binding.root.findNavController().navigate(
                            HomeFragmentDirections.actionNavigationCurrenciesToCurrencyInfoFragment(
                                currencyData.iso4217Alpha
                            )
                        )
                    }
                }
            )

        binding.rvCurrenciesList.adapter = currenciesListAdapter
    }
}
