package com.example.currencyapp.ui.model

import android.content.Context
import com.example.currencyapp.R
import com.example.currencyapp.domain.model.rates.Currencies
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class CurrencyInfoResourceProvider @Inject constructor(@ApplicationContext val context: Context) {

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