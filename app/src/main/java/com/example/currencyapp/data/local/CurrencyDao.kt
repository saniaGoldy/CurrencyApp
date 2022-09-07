package com.example.currencyapp.data.local

import androidx.room.*
import com.example.currencyapp.data.local.entities.CurrencyDataEntity

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencydataentity")
    suspend fun getAll(): List<CurrencyDataEntity>

    @Query("SELECT * FROM currencydataentity WHERE iso4217Alpha LIKE :iso4217Alpha")
    suspend fun findById(iso4217Alpha: String): CurrencyDataEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(currenciesList: List<CurrencyDataEntity>)

    @Delete
    fun delete(currencyData: CurrencyDataEntity)

    @Update
    fun update(currenciesList: List<CurrencyDataEntity>)
}