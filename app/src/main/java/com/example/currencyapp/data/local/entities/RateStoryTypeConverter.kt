package com.example.currencyapp.data.local.entities

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class RateStoryTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromStoredString(storedStringJson: String): Map<String, Double> {
        val mapType: Type = object : TypeToken<Map<String, Double>>() {}.type
        return gson.fromJson(storedStringJson, mapType)
    }

    @TypeConverter()
    fun fromMap(value: Map<String, Double>): String {
        return gson.toJson(value)
    }
}
