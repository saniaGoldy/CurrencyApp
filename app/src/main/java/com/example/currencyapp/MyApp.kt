package com.example.currencyapp

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

const val TAG = "MyApp"

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

@HiltAndroidApp
class MyApp : Application()