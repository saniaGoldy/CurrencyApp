package com.example.currencyapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyapp.data.local.entities.CurrencyDataEntity

@Database(entities = [CurrencyDataEntity::class], version = 1)
abstract class LocalDB : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}