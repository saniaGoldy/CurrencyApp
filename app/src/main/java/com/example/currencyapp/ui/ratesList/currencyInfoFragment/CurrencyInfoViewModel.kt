package com.example.currencyapp.ui.ratesList.currencyInfoFragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.TAG
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.repository.rates.RatesRepository
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyInfoViewModel @Inject constructor(
    private val repository: RatesRepository,
    context: Application
) : BaseViewModel(context){

    private val _currency = MutableLiveData<DataState<CurrencyData>>(DataState.Default)
    val currency: LiveData<DataState<CurrencyData>>
        get() = _currency

    private val exceptionHandler = CoroutineExceptionHandler { _, error ->
        Log.e(TAG, error.toString())
        _currency.postValue(DataState.Failure())
    }

    fun loadCurrencyData(code: String){
        _currency.value = DataState.Loading
        viewModelScope.launch(exceptionHandler) {
            _currency.postValue(
                repository.getCurrencyByCode(code).let {
                    if (it.isSuccess){
                        DataState.Success(it.getOrNull()!!)
                    }else{
                        DataState.Failure(it.exceptionOrNull().toString())
                    }
                }
            )
        }
    }
}