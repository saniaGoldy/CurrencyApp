package com.example.currencyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.currencyapp.databinding.ActivityMainBinding
import com.example.currencyapp.model.CurrencyFluctuation
import com.example.currencyapp.repository.MainRepository

const val TAG = "MyApp"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var currenciesListAdapter: CurrenciesListAdapter

    private val viewModel:MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUsersList()
        viewModel.fetchDataFromRepo()

        viewModel.currenciesAPIResponseResult.observe(this){ result ->
            if (result.isSuccess){
                Log.d(TAG, "observer: ${result.getOrNull()}")
                currenciesListAdapter.currenciesList = result.getOrNull()!!
            }else{
                Log.e(TAG, "observer: ${result.exceptionOrNull()}")
            }
        }
    }

    private fun setupUsersList() = binding.rvCurrenciesList.apply {
        currenciesListAdapter = CurrenciesListAdapter()
        adapter = currenciesListAdapter
    }
}