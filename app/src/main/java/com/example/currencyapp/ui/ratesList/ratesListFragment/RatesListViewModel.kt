package com.example.currencyapp.ui.ratesList.ratesListFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.TAG
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.model.rates.RatesListSettings
import com.example.currencyapp.domain.usecases.rates.RatesListUseCase
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RatesListViewModel @Inject constructor(
    private val interactor: RatesListUseCase,
    context: Context
) : BaseViewModel(context) {

    val ratesSettings: LiveData<RatesListSettings> = interactor.fetchRatesSettings()

    private val _ratesDataState =
        MutableLiveData<DataState<List<CurrencyData>>>()

    val ratesDataState: LiveData<DataState<List<CurrencyData>>>
        get() = _ratesDataState

    fun updateDataState() {
        _ratesDataState.postValue(DataState.Loading)
        Log.d(TAG, "Rates View model updateDataState")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val result = interactor.fetchRatesList()

                _ratesDataState.postValue(
                    if (result.isSuccess) {
                        DataState.Success(result.getOrNull()!!)
                    } else {
                        DataState.Failure(result.exceptionOrNull()?.message.toString())
                    }
                )
            }
        }
    }
}