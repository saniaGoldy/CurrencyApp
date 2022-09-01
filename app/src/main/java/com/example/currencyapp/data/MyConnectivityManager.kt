package com.example.currencyapp.data

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContentProviderCompat.requireContext

object MyConnectivityManager {
    fun checkConnectivity(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}