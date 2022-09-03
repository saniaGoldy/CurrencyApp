package com.example.currencyapp.ui.news

import android.app.Application
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.TAG
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.dataStore
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.domain.services.ConnectivityObserver
import com.example.currencyapp.ui.BaseViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: MainRepository,
    private val context: Application
) : BaseViewModel(context) {

    private val searchSettings = MutableLiveData<SearchSettings>()

    init {
        loadSettingsFromPrefs()
    }

    private val _news = MutableLiveData<Result<List<Data>>>()

    val news: LiveData<Result<List<Data>>>
        get() = _news


    val isLoading = MutableLiveData(false)

    val areNewsUpToDate = MutableLiveData(false)

    fun fetchNews() {
        if (networkStatus.value == ConnectivityObserver.Status.Available)
            isLoading.value = true

        try {
            viewModelScope.launch(Dispatchers.IO) {
                _news.postValue(repository.makeNewsQuery(searchSettings.value ?: SearchSettings()))
            }
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, "fetchNews: ${e.printStackTrace()}")
        }
    }

    fun setSearchSettings(settings: SearchSettings) {
        if (searchSettings.value != settings) {
            searchSettings.value = settings
            areNewsUpToDate.value = false
            saveSettingsToPrefs(settings)
        }
    }

    private val gson = Gson()

    private fun loadSettingsFromPrefs() {
        viewModelScope.launch {
            Log.d(TAG, "loadSettingsFromPrefs")
            context.dataStore.data.map { settingsPref ->
                val settingsJson = settingsPref[SETTINGS_PREF_KEY] ?: ""
                Log.d(TAG, "loadSettingsFromPrefs map: $settingsJson")

                searchSettings.postValue(gson.fromJson(settingsJson, SearchSettings::class.java))
            }.collect { Log.d(TAG, "loadSettingsFromPrefs collect: $it") }
        }
    }

    private fun saveSettingsToPrefs(settings: SearchSettings) {
        Log.d(TAG, "saveSettingsToPrefs")
        viewModelScope.launch {
            context.dataStore.edit {
                it[SETTINGS_PREF_KEY] = gson.toJson(settings)
                Log.d(TAG, "saveSettingsToPrefs: ${it[SETTINGS_PREF_KEY]}")
            }
        }
    }

    companion object {
        private val SETTINGS_PREF_KEY = stringPreferencesKey("searchSettings")
    }
}