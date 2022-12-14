package com.example.currencyapp.ui.ratesList.ratesListSettingsDialog

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.TAG
import com.example.currencyapp.domain.model.rates.RatesListSettings
import com.example.currencyapp.domain.services.NetworkConnectivityObserver
import com.example.currencyapp.domain.usecases.rates.RatesSettingsEditUseCase
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class RatesSettingsViewModel @Inject constructor(
    private val interactor: RatesSettingsEditUseCase,
    connectivityObserver: NetworkConnectivityObserver
) : BaseViewModel(connectivityObserver) {

    fun updateRatesListSettings(settings: RatesListSettings) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d(TAG, "updateRatesListSettings: $settings")
                interactor.updateSettings(settings)
            }
        }
    }
}