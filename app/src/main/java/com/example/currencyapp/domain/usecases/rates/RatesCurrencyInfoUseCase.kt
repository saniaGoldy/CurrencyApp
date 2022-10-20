package com.example.currencyapp.domain.usecases.rates

import androidx.compose.material.rememberDismissState
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.repository.RatesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

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