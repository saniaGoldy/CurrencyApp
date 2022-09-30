package com.example.currencyapp.ui.ratesList.ratesListFragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.TAG
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.DataWithErrorInfo
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.model.rates.RatesListSettings
import com.example.currencyapp.domain.usecases.rates.RatesListUseCase
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatesListViewModel @Inject constructor(
    private val interactor: RatesListUseCase,
    @ApplicationContext context: Context
) : BaseViewModel(context) {

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
                when (result) {
                    is DataWithErrorInfo.Failure -> DataState.Failure(result.errorInfo)
                    is DataWithErrorInfo.Success -> DataState.Success(result.value)
                    is DataWithErrorInfo.SuccessWithErrorInfo -> DataState.Success(
                        result.value,
                        result.errorInfo
                    )
                }
            )

        }
    }
}