package com.example.currencyapp.domain.usecases.rates

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
            runCatching {
                ratesRepository.getCurrencyByCode(code)
            }
        }
    }
}