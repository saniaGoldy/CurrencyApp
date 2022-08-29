package com.example.currencyapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.model.CurrencyFluctuation
import com.example.currencyapp.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var _currenciesList: LiveData<List<CurrencyFluctuation>> = fetchDataFromLocalDB()

    val currenciesList: LiveData<List<CurrencyFluctuation>>
        get() = _currenciesList

    val errorResult = MutableLiveData<Throwable>()

    init {
        fetchDataFromAPI()
    }

    private fun fetchDataFromAPI() {
        MainRepository.makeCurrencyQuery(object : MainRepository.ResponseProcessor {
            override fun process(result: Result<List<CurrencyFluctuation>>) {
                viewModelScope.launch(Dispatchers.IO) {
                    val currencies = result.getOrNull()
                    if (currencies != null)
                        MainRepository.getLocalDB(getApplication<Application>().applicationContext)
                            .currencyDao().insertAll(currencies).also {
                                Log.d(
                                    TAG,
                                    "process: Saving data to local db..."
                                )
                            }
                    else
                        errorResult.value = result.exceptionOrNull()
                }
            }
        })
    }

    private fun fetchDataFromLocalDB(): LiveData<List<CurrencyFluctuation>> {
        return MainRepository.getLocalDB(getApplication<Application>().applicationContext)
            .currencyDao().getAll().also {
                Log.d(
                    TAG,
                    "fetchDataFromLocalDB"
                )
            }

    }


}