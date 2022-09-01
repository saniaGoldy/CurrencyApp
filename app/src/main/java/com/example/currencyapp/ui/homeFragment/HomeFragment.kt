package com.example.currencyapp.ui.homeFragment

import android.content.Context
import android.net.ConnectivityManager
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
import com.example.currencyapp.databinding.FragmentHomeBinding
import com.example.currencyapp.domain.model.CurrencyFluctuation
import com.example.currencyapp.domain.services.ConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var currenciesListAdapter: CurrenciesListAdapter

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var connectivityStatus = ConnectivityObserver.Status.Available

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

        setupUsersList()

        binding.progressBar.isVisible = true

        viewModel.networkStatus.observe(viewLifecycleOwner){ status ->
            connectivityStatus = status.also { Log.d(TAG, "ConnectivityStatus: $it") }
            binding.tvNoInternetConnection.isVisible = status != ConnectivityObserver.Status.Available
        }

        viewModel.currenciesList.observe(viewLifecycleOwner) { currencies ->
            binding.progressBar.isVisible = false
            currenciesListAdapter.currenciesList = currencies
        }

        viewModel.errorResult.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            Log.e(TAG, "observer: $it")

            Toast.makeText(
                this.requireContext(),
                if (connectivityStatus == ConnectivityObserver.Status.Available) getString(R.string.standart_error_message) else getString(
                    R.string.no_internet_connection_error_message
                ),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setupUsersList() {
        currenciesListAdapter =
            CurrenciesListAdapter(object : CurrenciesListAdapter.ItemClickedAction {
                override fun run(currencyFluctuation: CurrencyFluctuation) {
                    binding.root.findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                            currencyFluctuation
                        )
                    )
                }
            })

        binding.rvCurrenciesList.adapter = currenciesListAdapter
    }
}
