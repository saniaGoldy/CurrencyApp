package com.example.currencyapp.ui.ratesList.currencyInfoFragment

import android.app.Application
import com.example.currencyapp.domain.repository.rates.RatesRepository
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencyInfoViewModel @Inject constructor(
    private val repository: RatesRepository,
    context: Application
) : BaseViewModel(context)