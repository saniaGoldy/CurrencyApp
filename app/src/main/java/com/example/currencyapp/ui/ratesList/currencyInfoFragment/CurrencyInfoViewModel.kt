package com.example.currencyapp.ui.ratesList.currencyInfoFragment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.R
import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.rates.Currencies
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.usecases.rates.RatesCurrencyInfoUseCase
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyInfoViewModel @Inject constructor(
    private val interactor: RatesCurrencyInfoUseCase,
    @ApplicationContext private val context: Context
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

    fun getTvChartFragmentTitle(code: String): String {
        val currency = Currencies.valueOf(code)

        return context.getString(
            R.string.title_rates_chart_for,
            currency.fullName
        )
    }

    fun getTvChartFragmentDetails(baseCurrencyCode: String): String {
        val currency = Currencies.valueOf(baseCurrencyCode)

        return context.getString(
            R.string.based_on_detailes,
            currency.fullName
        )
    }

    fun getFromToLabel(dates: List<String>): String {
        return context.getString(R.string.from_to, dates[0], dates[dates.lastIndex])
    }

}