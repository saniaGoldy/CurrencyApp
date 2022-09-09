package com.example.currencyapp.domain

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar

object CurrentDateData {
    private const val OBSERVED_TIME_GAP = 7

    @SuppressLint("SimpleDateFormat")
    private val dataFormat = SimpleDateFormat("yyyy-MM-dd")

    val currentDate: String
        get() = dataFormat.format(Calendar.getInstance().time)

    val yesterdaysDate: String
        get() {
            val calendar = Calendar.getInstance().apply { add(Calendar.DATE, -1) }
            return dataFormat.format(calendar.time)
        }

    val startDate: String
        get() {
            val calendar = Calendar.getInstance().apply { add(Calendar.DATE, -OBSERVED_TIME_GAP) }
            return dataFormat.format(calendar.time)
        }


    /**
     * @param date in format "yyyy-MM-dd"
     * @return date in format "dd.MM"
     */
    fun formatToSimpleDate(date: String): String {
        date.split('-').apply {
            return "${this[2]}.${this[1]}"
        }
    }
}