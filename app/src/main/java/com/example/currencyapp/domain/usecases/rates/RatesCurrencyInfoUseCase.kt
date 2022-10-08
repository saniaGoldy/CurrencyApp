package com.example.currencyapp.domain.usecases.rates

import android.util.Log
import com.example.currencyapp.TAG
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.repository.RatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RatesCurrencyInfoUseCase @Inject constructor(
    val ratesRepository: RatesRepository
) {

    suspend fun getCurrencyByCode(code: String): Result<CurrencyData> {
        return withContext(Dispatchers.IO) {
            try {
                ratesRepository.getCurrencyByCode(code).let {
                    Result.success(it)
                }
            } catch (ex: Exception) {
                Log.d(TAG, "fetchNewsList: ${ex.message}")
                Result.failure(ex)
            }
        }
    }
}