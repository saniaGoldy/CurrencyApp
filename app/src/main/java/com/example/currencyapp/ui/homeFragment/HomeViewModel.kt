package com.example.currencyapp.ui.homeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.domain.model.CurrencyFluctuation
import com.example.currencyapp.domain.repository.IResponseProcessor
import com.example.currencyapp.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private var _currenciesList: LiveData<List<CurrencyFluctuation>> =
        repository.fetchDataFromLocalDB()

    val currenciesList: LiveData<List<CurrencyFluctuation>>
        get() = _currenciesList

    val errorResult = MutableLiveData<Throwable>()

    init {
        fetchDataFromAPI()
    }

    private fun fetchDataFromAPI() {
        repository.makeCurrencyQuery(object : IResponseProcessor {

            override fun process(result: Result<List<CurrencyFluctuation>>) {

                val currencies = result.getOrNull()

                if (currencies != null) {
                    viewModelScope.launch(Dispatchers.IO) {
                        repository.saveDataToLocalDB(currencies)
                    }
                } else {
                    errorResult.value = result.exceptionOrNull()
                }

            }
        })
    }


}