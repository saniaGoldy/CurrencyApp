package com.example.currencyapp.data.repository.preferences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.currencyapp.TAG
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.domain.CurrentDateData
import com.example.currencyapp.ui.ratesList.model.RatesListSettings
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferencesRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    PreferencesRepository {
    private val gson = Gson()

    override fun loadNewsSettings(): Flow<SearchSettings> {
        Log.d(TAG, "loadNewsSettings")
        return dataStore.data.map { preferences ->
            preferences[NEWS_SETTINGS_PREF_KEY]?.let { settingsJson: String ->
                gson.fromJson(settingsJson, SearchSettings::class.java).also {
                    Log.d(TAG, "loadNewsSettings: $it")
                }
            } ?: SearchSettings().also { Log.d(TAG, "loadNewsSettings: default settings") }
        }
    }


    override suspend fun saveNewsSettings(settings: SearchSettings) {
        Log.d(TAG, "saveNewsSettings")
        dataStore.edit {
            it[NEWS_SETTINGS_PREF_KEY] = gson.toJson(settings)
            Log.d(TAG, "saveNewsSettings: ${it[NEWS_SETTINGS_PREF_KEY]}")
        }
    }

    override suspend fun isRatesUpToDate(): Boolean? {
        Log.d(TAG, "isRatesUpToDate")
        return dataStore.data.first()[LAST_RATES_UPDATE_DATE_KEY]?.let { it == CurrentDateData.currentDate }
    }

    override suspend fun saveRatesUpdateDate() {
        Log.d(TAG, "saveRatesUpdateDate")
        dataStore.edit {
            it[LAST_RATES_UPDATE_DATE_KEY] = CurrentDateData.currentDate
        }
    }

    override fun loadRatesListSettings(): Flow<RatesListSettings> {
        Log.d(TAG, "loadRatesSettings")

        return dataStore.data.map { preferences ->
            preferences[RATES_SETTINGS_PREF_KEY]?.let { settingsJson: String ->
                gson.fromJson(settingsJson, RatesListSettings::class.java).also {
                    Log.d(TAG, "loadRatesListSettings: $it")
                }
            } ?: RatesListSettings().also { Log.d(TAG, "loadRatesListSettings: default settings") }
        }
    }


    override suspend fun saveRatesListSettings(settings: RatesListSettings) {
        Log.d(TAG, "saveRatesSettings")

        dataStore.edit {
            it[RATES_SETTINGS_PREF_KEY] = gson.toJson(settings)
            Log.d(TAG, "saveRatesSettings: ${it[RATES_SETTINGS_PREF_KEY]}")
        }
    }

    companion object {
        private val NEWS_SETTINGS_PREF_KEY = stringPreferencesKey("searchSettings")
        private val LAST_RATES_UPDATE_DATE_KEY = stringPreferencesKey("lastRatesUpdateDate")
        private val RATES_SETTINGS_PREF_KEY = stringPreferencesKey("ratesSettings")
    }
}