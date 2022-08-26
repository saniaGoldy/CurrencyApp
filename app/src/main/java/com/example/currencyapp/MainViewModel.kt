package com.example.currencyapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currencyapp.currencyAPI.entities.CurrenciesFluctuationsResponse
import com.example.currencyapp.model.CurrencyFluctuation
import com.example.currencyapp.repository.MainRepository

class MainViewModel : ViewModel() {

    private val _currenciesAPIResponseResult = MutableLiveData<Result<List<CurrencyFluctuation>>>()

    val currenciesAPIResponseResult
        get() = _currenciesAPIResponseResult

    fun fetchDataFromRepo() {
        MainRepository.makeCurrencyQuery(object : MainRepository.ResponseProcessor{
            override fun process(result: Result<List<CurrencyFluctuation>>) {
                _currenciesAPIResponseResult.value = result
            }
        })
    }

}