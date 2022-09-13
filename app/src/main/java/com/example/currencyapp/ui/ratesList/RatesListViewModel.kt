package com.example.currencyapp.ui.ratesList

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.TAG
import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.domain.repository.MainRepository.DataState
import com.example.currencyapp.ui.BaseViewModel
import com.example.currencyapp.ui.ratesList.model.RatesListSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class RatesListViewModel @Inject constructor(
    private val repository: MainRepository,
    context: Application
) : BaseViewModel(context) {

    private val _ratesDataState =
        MutableLiveData<DataState<List<CurrencyData>>>(DataState.Default)
    val ratesDataState: LiveData<DataState<List<CurrencyData>>>
        get() = _ratesDataState

    private val _ratesSettings = MutableLiveData(RatesListSettings())
    val ratesSettings: LiveData<RatesListSettings>
        get() = _ratesSettings

    private val exceptionHandler = CoroutineExceptionHandler { _, error ->
        Log.d(TAG, error.toString())
        _ratesDataState.postValue(DataState.Failure())
    }

    init {
        _ratesDataState.value = DataState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val settings = async { repository.loadRatesListSettings() }
            updateDataState(settings.await())
        }
    }

    private suspend fun updateDataState(ratesListSettings: RatesListSettings) =
        viewModelScope.launch(exceptionHandler) {
            _ratesDataState.postValue(DataState.Loading)

            val result = repository.fetchCurrenciesList(
                viewModelScope,
                ratesListSettings.currencyCode
            )

            _ratesDataState.postValue(result)
        }

    fun updateRatesListSettings(settings: RatesListSettings) =
        viewModelScope.launch(exceptionHandler) {
            _ratesSettings.postValue(settings)


            repository.saveRatesListSettings(settings, viewModelScope)
            updateDataState(settings)


        }
}