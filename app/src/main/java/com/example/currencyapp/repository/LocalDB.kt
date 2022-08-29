package com.example.currencyapp.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyapp.model.CurrencyFluctuation

@Database(entities = [CurrencyFluctuation::class], version = 1)
abstract class LocalDB: RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}