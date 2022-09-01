package com.example.currencyapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.currencyapp.domain.model.CurrencyFluctuation

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currencyfluctuation")
    suspend fun getAll(): List<CurrencyFluctuation>

    @Query("SELECT * FROM currencyfluctuation WHERE iso4217Alpha LIKE :iso4217Alpha")
    suspend fun findById(iso4217Alpha: String): CurrencyFluctuation

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(currenciesList: List<CurrencyFluctuation>)

    @Delete
    fun delete(currencyFluctuation: CurrencyFluctuation)

    @Update
    fun update(currenciesList: List<CurrencyFluctuation>)
}