package com.example.currencyapp.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.currencyapp.TAG
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.dataStore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PreferencesRepository(val context: Context) {
    private val gson = Gson()

    suspend fun loadSettings(): SearchSettings {
        Log.d(TAG, "loadSettings")
        val settingsJson = context.dataStore.data.first()[SETTINGS_PREF_KEY] ?: ""
        return gson.fromJson(settingsJson, SearchSettings::class.java)
    }


    fun saveSettings(settings: SearchSettings, scope: CoroutineScope) {
        Log.d(TAG, "saveSettings")
        scope.launch(Dispatchers.IO) {
            context.dataStore.edit {
                it[SETTINGS_PREF_KEY] = gson.toJson(settings)
                Log.d(TAG, "saveSettings: ${it[SETTINGS_PREF_KEY]}")
            }
        }
    }

    suspend fun isRatesUpToDate(): Boolean {
        Log.d(TAG, "isRatesUpToDate")
        return context.dataStore.data.first()[LAST_RATES_UPDATE_DATE_KEY]?.let { it == CurrentDateData.currentDate }
            ?: false
    }

    suspend fun saveRatesUpdateDate() {
        Log.d(TAG, "saveRatesUpdateDate")
        context.dataStore.edit {
            it[LAST_RATES_UPDATE_DATE_KEY] = CurrentDateData.currentDate
        }
    }

    companion object {
        private val SETTINGS_PREF_KEY = stringPreferencesKey("searchSettings")
        private val LAST_RATES_UPDATE_DATE_KEY = stringPreferencesKey("lastRatesUpdateDate")
    }
}