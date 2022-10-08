package com.example.currencyapp.domain.usecases.rates

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.repository.RatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RatesListUseCase @Inject constructor(
    ratesRepository: RatesRepository
) : RatesSettingsReadUseCase(ratesRepository) {

    suspend fun fetchRatesList(): Result<List<CurrencyData>> {
        return withContext(Dispatchers.IO) {
            try {
                ratesRepository.fetchCurrenciesList().let {
                    if (it.isNotEmpty()) {
                        Result.success(it)
                    } else {
                        Result.failure(NullPointerException("rates list is empty"))
                    }
                }
            } catch (ex: Exception) {
                Log.d(TAG, "fetchNewsList: ${ex.message}")
                Result.failure(ex)
            }
        }
    }
}