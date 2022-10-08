package com.example.currencyapp.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Test


internal class CurrentDateDataTest {

    @Test
    fun `format yyyy MM dd to dd MM`() {
        val date = "2022-09-20"
        assertThat(CurrentDateData.formatToSimpleDate(date)).isEqualTo("20.09")
    }
}