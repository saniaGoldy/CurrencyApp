package com.example.currencyapp.ui.homeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.domain.model.CurrencyFluctuation
import com.example.currencyapp.domain.repository.IResponseProcessor
import com.example.currencyapp.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
            _currenciesList.postValue(repository.fetchDataFromLocalDB())
        }
        //fetchDataFromAPI()
    }

    /*private fun fetchDataFromAPI() {
        repository.makeCurrencyQuery(object : IResponseProcessor {

            override fun process(result: Result<List<CurrencyFluctuation>>) {

                val currencies = result.getOrNull()

                if (currencies != null) {
                    repository.saveDataToLocalDB(currencies)
                } else {
                    errorResult.value = result.exceptionOrNull()
                }

            }
        })
    }*/


}