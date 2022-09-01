package com.example.currencyapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.currencyapp.domain.model.CurrencyFluctuation
import com.example.currencyapp.domain.repository.IResponseProcessor
import com.example.currencyapp.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

}