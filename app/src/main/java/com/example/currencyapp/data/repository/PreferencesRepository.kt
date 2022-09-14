package com.example.currencyapp.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.currencyapp.TAG
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.dataStore
import com.example.currencyapp.domain.CurrentDateData
import com.example.currencyapp.ui.ratesList.model.RatesListSettings
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PreferencesRepository(val context: Context) {
    private val gson = Gson()

    suspend fun loadNewsSettings(): SearchSettings {
        Log.d(TAG, "loadNewsSettings")
        return context.dataStore.data.first()[NEWS_SETTINGS_PREF_KEY]?.let {
            gson.fromJson(it, SearchSettings::class.java)
        } ?: SearchSettings()
    }


    fun saveNewsSettings(settings: SearchSettings, scope: CoroutineScope) {
        Log.d(TAG, "saveNewsSettings")
        scope.launch(Dispatchers.IO) {
            context.dataStore.edit {
                it[NEWS_SETTINGS_PREF_KEY] = gson.toJson(settings)
                Log.d(TAG, "saveNewsSettings: ${it[NEWS_SETTINGS_PREF_KEY]}")
            }
        }
    }

    suspend fun isRatesUpToDate(): Boolean? {
        Log.d(TAG, "isRatesUpToDate")
        return context.dataStore.data.first()[LAST_RATES_UPDATE_DATE_KEY]?.let { it == CurrentDateData.currentDate }
    }

    suspend fun saveRatesUpdateDate() {
        Log.d(TAG, "saveRatesUpdateDate")
        context.dataStore.edit {
            it[LAST_RATES_UPDATE_DATE_KEY] = CurrentDateData.currentDate
        }
    }

    fun loadRatesListSettings(): Flow<RatesListSettings> {
        Log.d(TAG, "loadRatesSettings")

        return context.dataStore.data.map { preferences ->
            preferences[RATES_SETTINGS_PREF_KEY]?.let { settingsJson: String ->
                gson.fromJson(settingsJson, RatesListSettings::class.java).also {
                    Log.d(TAG, "loadRatesListSettings: $it")
                }
            } ?: RatesListSettings().also { Log.d(TAG, "loadRatesListSettings: default settings") }
        }
    }


    fun saveRatesListSettings(settings: RatesListSettings, scope: CoroutineScope) {
        Log.d(TAG, "saveRatesSettings")
        scope.launch(Dispatchers.IO) {
            context.dataStore.edit {
                it[RATES_SETTINGS_PREF_KEY] = gson.toJson(settings)
                Log.d(TAG, "saveRatesSettings: ${it[RATES_SETTINGS_PREF_KEY]}")
            }
        }
    }

    companion object {
        private val NEWS_SETTINGS_PREF_KEY = stringPreferencesKey("searchSettings")
        private val LAST_RATES_UPDATE_DATE_KEY = stringPreferencesKey("lastRatesUpdateDate")
        private val RATES_SETTINGS_PREF_KEY = stringPreferencesKey("ratesSettings")
    }
}