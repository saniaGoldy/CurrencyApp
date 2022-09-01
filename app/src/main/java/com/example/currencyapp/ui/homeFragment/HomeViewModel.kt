package com.example.currencyapp.ui.homeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.domain.model.CurrencyFluctuation
import com.example.currencyapp.domain.repository.IResponseProcessor
import com.example.currencyapp.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private var _currenciesList: MutableLiveData<List<CurrencyFluctuation>> = MutableLiveData()

    val currenciesList: LiveData<List<CurrencyFluctuation>>
        get() = _currenciesList

    val errorResult = MutableLiveData<Throwable>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                _currenciesList.postValue(repository.fetchDataFromLocalDB())
            }

            val result = repository.makeCurrencyQuery()
            if (result.isSuccess){
                repository.saveDataToLocalDB(result.getOrNull()!!)
            }else{
                errorResult.postValue(result.exceptionOrNull())
            }

        }
    }

}