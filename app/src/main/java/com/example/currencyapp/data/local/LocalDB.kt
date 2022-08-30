package com.example.currencyapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyapp.data.local.CurrencyDao
import com.example.currencyapp.domain.model.CurrencyFluctuation

@Database(entities = [CurrencyFluctuation::class], version = 1)
abstract class LocalDB : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}