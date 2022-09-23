package com.example.currencyapp.data.local

import androidx.test.filters.SmallTest
import com.example.currencyapp.data.local.entities.CurrencyDataEntity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class CurrencyDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_db")
    lateinit var database: LocalDB
    private lateinit var dao: CurrencyDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.currencyDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertListOfCurrencies() = runTest {
        val currencyEntities = listOf(CurrencyDataEntity("UAH", 1.0, mapOf()))

        dao.insertAll(currencyEntities)
        assertThat(currencyEntities).isEqualTo(dao.getAll())
    }

    @Test
    fun deleteCurrencyEntry() = runTest {
        val currencyDataEntity = CurrencyDataEntity("UAH", 1.0, mapOf())
        val currencyEntities = listOf(currencyDataEntity)

        dao.insertAll(currencyEntities)
        dao.delete(currencyDataEntity)
        assertThat(dao.findById(currencyDataEntity.iso4217Alpha)).isNull()
    }

    @Test
    fun updateCurrencyEntry() = runTest {
        val currencyDataEntityOld = CurrencyDataEntity("UAH", 1.0, mapOf())
        val currencyDataEntityNew = CurrencyDataEntity("UAH", 1.0, mapOf("2022-09-20" to 1.0))

        dao.insertAll(listOf(currencyDataEntityOld))

        dao.update(listOf(currencyDataEntityNew))

        assertThat(dao.findById(currencyDataEntityOld.iso4217Alpha)).isEqualTo(currencyDataEntityNew)
    }
}