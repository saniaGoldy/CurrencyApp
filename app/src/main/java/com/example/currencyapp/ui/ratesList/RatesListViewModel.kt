package com.example.currencyapp.ui.ratesList

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.TAG
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.repository.rates.RatesRepository
import com.example.currencyapp.ui.BaseViewModel
import com.example.currencyapp.ui.ratesList.model.RatesListSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatesListViewModel @Inject constructor(
    private val repository: RatesRepository,
    context: Application
) : BaseViewModel(context) {


    private val _ratesDataState =
        MutableLiveData<DataState<List<CurrencyData>>>(DataState.Default)
    val ratesDataState: LiveData<DataState<List<CurrencyData>>>
        get() = _ratesDataState

    val ratesSettings: LiveData<RatesListSettings> = repository.loadRatesListSettings().asLiveData()

    private val exceptionHandler = CoroutineExceptionHandler { _, error ->
        Log.e(TAG, error.toString())
        _ratesDataState.postValue(DataState.Failure())
    }

    fun updateDataState() {
        Log.d(TAG, "Rates View model updateDataState")
        viewModelScope.launch(exceptionHandler) {
            _ratesDataState.postValue(DataState.Loading)

            val result = repository.fetchCurrenciesList()

            _ratesDataState.postValue(
                if(result.isSuccess){
                    DataState.Success(result.getOrNull()!!)
                }else{
                    DataState.Failure(result.exceptionOrNull()!!.stackTraceToString())
                }
            )
        }
    }

    fun updateRatesListSettings(settings: RatesListSettings) {
        viewModelScope.launch {
            Log.d(TAG, "updateRatesListSettings: $settings")
            repository.saveRatesListSettings(settings)
        }
    }
}