package com.example.currencyapp.ui.homeFragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.domain.repository.MainRepository.DataState
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MainRepository,
    context: Application
) : BaseViewModel(context) {

    private val _ratesDataState =
        MutableLiveData<DataState<List<CurrencyData>>>(DataState.Default)
    val ratesDataState: LiveData<DataState<List<CurrencyData>>>
        get() = _ratesDataState

    init {
        _ratesDataState.value = DataState.Loading

        viewModelScope.launch { updateDataState() }

        viewModelScope.launch(Dispatchers.IO) {
            repository.fetchCurrenciesList(this).let { dataState ->
                if (dataState is DataState.Success)
                    _ratesDataState.postValue(dataState)
            }
            updateDataState()
        }
    }

    private suspend fun updateDataState() {
        _ratesDataState.postValue(DataState.Loading)

        val result = repository.fetchCurrenciesList(viewModelScope)

        _ratesDataState.postValue(result)
    }
}