package com.example.currencyapp.ui.ratesList.currencyInfoFragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.usecases.rates.RatesCurrencyInfoUseCase
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyInfoViewModel @Inject constructor(
    private val interactor: RatesCurrencyInfoUseCase,
    context: Application
) : BaseViewModel(context) {

    private val _currency = MutableLiveData<DataState<CurrencyData>>(DataState.Default)
    val currency: LiveData<DataState<CurrencyData>>
        get() = _currency

    fun loadCurrencyData(code: String) {
        _currency.value = DataState.Loading
        viewModelScope.launch {

            _currency.postValue(
                interactor.getCurrencyByCode(code).let {
                    if (it.isSuccess) {
                        DataState.Success(it.getOrNull()!!)
                    } else {
                        DataState.Failure(it.exceptionOrNull().toString())
                    }
                }
            )

        }
    }
}