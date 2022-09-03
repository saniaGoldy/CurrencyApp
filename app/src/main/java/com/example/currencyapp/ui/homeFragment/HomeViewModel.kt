package com.example.currencyapp.ui.homeFragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.domain.model.CurrencyFluctuation
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.domain.services.ConnectivityObserver
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

    private var _currenciesList: MutableLiveData<List<CurrencyFluctuation>> = MutableLiveData()

    val currenciesList: LiveData<List<CurrencyFluctuation>>
        get() = _currenciesList

    val errorResult = MutableLiveData<Throwable>()

    init {

        viewModelScope.launch(Dispatchers.IO) {
            launch {
                _currenciesList.postValue(repository.fetchDataFromLocalDB())
            }

            if (networkStatus.value == ConnectivityObserver.Status.Available) {
                val result = repository.makeCurrencyQuery()
                if (result.isSuccess) {
                    repository.saveDataToLocalDB(result.getOrNull()!!)
                } else {
                    errorResult.postValue(result.exceptionOrNull())
                }
            }
        }

    }

}