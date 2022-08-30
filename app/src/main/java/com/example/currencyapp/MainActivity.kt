package com.example.currencyapp

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.currencyapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

const val TAG = "MyApp"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currenciesListAdapter: CurrenciesListAdapter

    private val viewModel: MainViewModel by viewModels()
    //private lateinit var viewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUsersList()
        binding.progressBar.isVisible = true
        viewModel.fetchDataFromRepo()

        viewModel.currenciesAPIResponseResult.observe(this) { result ->
            binding.progressBar.isVisible = false

            if (result.isSuccess) {
                currenciesListAdapter.currenciesList = result.getOrNull()!!

            } else {

                Log.e(TAG, "observer: ${result.exceptionOrNull()}")

                Toast.makeText(
                    this,
                    if (checkConnectivity()) getString(R.string.standart_error_message) else getString(
                        R.string.no_internet_connection_error_message
                    ),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupUsersList() = binding.rvCurrenciesList.apply {
        currenciesListAdapter = CurrenciesListAdapter()
        adapter = currenciesListAdapter
    }

    private fun checkConnectivity(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}