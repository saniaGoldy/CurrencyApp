package com.example.currencyapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currencyapp.domain.repository.IResponseProcessor
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.domain.model.CurrencyFluctuation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private val _currenciesAPIResponseResult = MutableLiveData<Result<List<CurrencyFluctuation>>>()

    val currenciesAPIResponseResult
        get() = _currenciesAPIResponseResult

    fun fetchDataFromRepo() {
        repository.makeCurrencyQuery(object : IResponseProcessor {
            override fun process(result: Result<List<CurrencyFluctuation>>) {
                _currenciesAPIResponseResult.value = result
            }
        })
    }

}