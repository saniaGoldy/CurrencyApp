package com.example.currencyapp.domain.usecases.rates

import com.example.currencyapp.domain.model.InconsistentData
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.repository.RatesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RatesListUseCase @Inject constructor(
    ratesRepository: RatesRepository
) : RatesSettingsReadUseCase(ratesRepository) {

    suspend fun fetchRatesList(): InconsistentData<List<CurrencyData>> {
        return withContext(Dispatchers.IO) {
            runCatching {
                ratesRepository.fetchCurrenciesList()
            }.fold(
                { it },
                { InconsistentData.Failure() }
            )
        }
    }
}