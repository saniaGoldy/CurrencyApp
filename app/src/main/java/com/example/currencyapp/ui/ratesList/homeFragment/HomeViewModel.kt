package com.example.currencyapp.ui.ratesList.homeFragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.TAG
import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.domain.repository.MainRepository.DataState
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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

    private val exceptionHandler = CoroutineExceptionHandler { context, error ->
        Log.d(TAG, error.toString())
        _ratesDataState.postValue(DataState.Failure())
    }

    init {
        _ratesDataState.value = DataState.Loading

        viewModelScope.launch(exceptionHandler) { updateDataState() }
    }

    private suspend fun updateDataState() {
        _ratesDataState.postValue(DataState.Loading)

        val result = repository.fetchCurrenciesList(viewModelScope)

        _ratesDataState.postValue(result)
    }
}