package com.example.currencyapp.domain.usecases.rates

import com.example.currencyapp.domain.model.rates.RatesListSettings
import com.example.currencyapp.domain.repository.RatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RatesSettingsEditUseCase @Inject constructor(
    ratesRepository: RatesRepository
) : RatesSettingsReadUseCase(ratesRepository) {

    suspend fun updateSettings(settings: RatesListSettings) {
        withContext(Dispatchers.IO) {
            ratesRepository.saveRatesListSettings(settings)
        }
    }
}