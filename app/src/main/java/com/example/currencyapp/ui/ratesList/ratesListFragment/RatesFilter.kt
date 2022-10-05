package com.example.currencyapp.ui.ratesList.ratesListFragment

import com.example.currencyapp.domain.model.DataState
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.ui.model.Filter

class RatesFilter(private val cashedListState: DataState<List<CurrencyData>>?) :
    Filter<CurrencyData> {
    override fun filter(keyword: String?): List<CurrencyData> {
        return if (cashedListState is DataState.Success) {
            if (!keyword.isNullOrEmpty())
                cashedListState.result.filter {
                    it.currency.name.contains(keyword, true) || it.currency.fullName.contains(
                        keyword,
                        true
                    )
                }
            else
                cashedListState.result
        } else
            listOf()
    }
}