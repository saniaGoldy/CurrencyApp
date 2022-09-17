package com.example.currencyapp.domain.usecases.rates

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.currencyapp.TAG
import com.example.currencyapp.domain.model.rates.RatesListSettings
import com.example.currencyapp.domain.repository.RatesRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

open class RatesSettingsReadUseCase @Inject constructor(
    val ratesRepository: RatesRepository
) {

    private val exceptionHandler = CoroutineExceptionHandler { context, error ->
        Log.d(TAG, error.toString())
    }

    fun fetchRatesSettings(): LiveData<RatesListSettings> {
        return ratesRepository.loadRatesListSettings().asLiveData(exceptionHandler)
    }
}