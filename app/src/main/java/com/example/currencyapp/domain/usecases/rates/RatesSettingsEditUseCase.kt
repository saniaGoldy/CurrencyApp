package com.example.currencyapp.domain.usecases.rates

import com.example.currencyapp.domain.model.rates.RatesListSettings
import com.example.currencyapp.domain.repository.RatesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RatesSettingsEditUseCase @Inject constructor(
    ratesRepository: RatesRepository,
    private val dispatcher: CoroutineDispatcher
) : RatesSettingsReadUseCase(ratesRepository) {

    suspend fun updateSettings(settings: RatesListSettings) {
        withContext(dispatcher) {
            ratesRepository.saveRatesListSettings(settings)
        }
    }
}