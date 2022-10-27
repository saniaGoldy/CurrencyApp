package com.example.currencyapp.ui.ratesList.ratesListFragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.TAG
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.model.rates.RatesListSettings
import com.example.currencyapp.domain.services.NetworkConnectivityObserver
import com.example.currencyapp.domain.usecases.rates.RatesListUseCase
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class RatesListViewModel @Inject constructor(
    private val interactor: RatesListUseCase,
    networkConnectivityObserver: NetworkConnectivityObserver
) : BaseViewModel(networkConnectivityObserver) {

    val ratesSettings: LiveData<RatesListSettings> = interactor.fetchRatesSettings()

    private val _ratesDataState =
        MutableLiveData<DataState<List<CurrencyData>>>(DataState.Default)

    val ratesDataState: LiveData<DataState<List<CurrencyData>>>
        get() = _ratesDataState

    fun updateDataState() {
        _ratesDataState.value = DataState.Loading
        Log.d(TAG, "Rates View model updateDataState")
        viewModelScope.launch {
            val result = interactor.fetchRatesList()

            _ratesDataState.postValue(
                if (result.isSuccess) {
                    result.getOrNull()?.let {
                        if (it.isUpToDate) {
                            DataState.Success(it.data)
                        } else {
                            DataState.Success(it.data, "Rates are not up to date")
                        }
                    }
                } else {
                    DataState.Failure(result.exceptionOrNull().toString())
                }
            )
        }
    }
}