package com.example.currencyapp.ui.homeFragment

import android.app.Application
import androidx.lifecycle.*
import com.example.currencyapp.model.CurrencyFluctuation
import com.example.currencyapp.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel (application: Application) : AndroidViewModel(application) {

    private var _currenciesList: LiveData<List<CurrencyFluctuation>> =
        MainRepository.fetchDataFromLocalDB(application.applicationContext)

    val currenciesList: LiveData<List<CurrencyFluctuation>>
        get() = _currenciesList

    val errorResult = MutableLiveData<Throwable>()

    init {
        fetchDataFromAPI()
    }

    private fun fetchDataFromAPI() {
        MainRepository.makeCurrencyQuery(object : MainRepository.ResponseProcessor {

            override fun process(result: Result<List<CurrencyFluctuation>>) {

                val currencies = result.getOrNull()

                if (currencies != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        MainRepository.saveDataToLocalDB(
                            getApplication<Application>().applicationContext,
                            currencies
                        )
                    }
                } else {
                    errorResult.value = result.exceptionOrNull()
                }

            }
        })
    }


}