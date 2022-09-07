package com.example.currencyapp.ui.currencyInfoFragment

import android.app.Application
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencyInfoViewModel @Inject constructor(
    private val repository: MainRepository,
    context: Application
) : BaseViewModel(context) {

}