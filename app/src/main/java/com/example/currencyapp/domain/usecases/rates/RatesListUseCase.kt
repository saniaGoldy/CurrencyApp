package com.example.currencyapp.domain.usecases.rates

import com.example.currencyapp.domain.model.UpdatableData
import com.example.currencyapp.domain.model.rates.CurrencyData
import com.example.currencyapp.domain.repository.RatesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RatesListUseCase @Inject constructor(
    ratesRepository: RatesRepository,
    private val dispatcher: CoroutineDispatcher
) : RatesSettingsReadUseCase(ratesRepository) {

    suspend fun fetchRatesList(): Result<UpdatableData<List<CurrencyData>>> {
        return withContext(dispatcher) {
            runCatching {
                ratesRepository.fetchCurrenciesList()
            }
        }
    }
}