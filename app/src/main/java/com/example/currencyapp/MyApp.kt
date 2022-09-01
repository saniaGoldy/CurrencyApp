package com.example.currencyapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

const val TAG = "MyApp"

@HiltAndroidApp
class MyApp : Application()