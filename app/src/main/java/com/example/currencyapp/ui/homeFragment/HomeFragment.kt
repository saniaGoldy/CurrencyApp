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
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.R
import com.example.currencyapp.TAG
import com.example.currencyapp.databinding.FragmentHomeBinding
import com.example.currencyapp.model.CurrencyFluctuation

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

        setupUsersList()

        binding.progressBar.isVisible = true

        viewModel.currenciesList.observe(viewLifecycleOwner) { currencies ->
            binding.progressBar.isVisible = false
            currenciesListAdapter.currenciesList = currencies
        }

        viewModel.errorResult.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = false
            Log.e(TAG, "observer: $it")

            Toast.makeText(
                this.requireContext(),
                if (checkConnectivity()) getString(R.string.standart_error_message) else getString(
                    R.string.no_internet_connection_error_message
                ),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setupUsersList(): RecyclerView {
        return binding.rvCurrenciesList.apply {

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
            adapter = currenciesListAdapter
        }
    }

    private fun checkConnectivity(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}
