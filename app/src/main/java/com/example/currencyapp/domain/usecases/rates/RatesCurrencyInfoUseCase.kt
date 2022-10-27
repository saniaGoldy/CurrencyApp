package com.example.currencyapp.domain.usecases.rates

import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.repository.RatesRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class RatesCurrencyInfoUseCase @Inject constructor(
    val ratesRepository: RatesRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun getCurrencyByCode(code: String): Result<CurrencyData> {
        return withContext(dispatcher) {
            runCatching {
                ratesRepository.getCurrencyByCode(code)
            }
        }
    }
}