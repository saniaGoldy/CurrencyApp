package com.example.currencyapp.ui.homeFragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.domain.model.CurrencyFluctuation
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.domain.services.ConnectivityObserver
import com.example.currencyapp.domain.services.NetworkConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MainRepository,
    context: Application
) : ViewModel() {

    private var _currenciesList: MutableLiveData<List<CurrencyFluctuation>> = MutableLiveData()

    val currenciesList: LiveData<List<CurrencyFluctuation>>
        get() = _currenciesList

    val errorResult = MutableLiveData<Throwable>()

    private val _networkState = MutableLiveData(ConnectivityObserver.Status.Unavailable)

    val networkStatus: LiveData<ConnectivityObserver.Status>
        get() = _networkState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                _currenciesList.postValue(repository.fetchDataFromLocalDB())
            }

            val result = repository.makeCurrencyQuery()
            if (result.isSuccess) {
                repository.saveDataToLocalDB(result.getOrNull()!!)
            } else {
                errorResult.postValue(result.exceptionOrNull())
            }
        }

        NetworkConnectivityObserver(context).observe().onEach {
            _networkState.value = it
        }.launchIn(viewModelScope)
    }

}