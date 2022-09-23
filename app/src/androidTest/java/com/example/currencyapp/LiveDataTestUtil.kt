package com.example.currencyapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class LiveDataTestUtil<T> {
    fun LiveData<T>.isValueGetsEmitted(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        emissionChecker: EmissionChecker
    ): Boolean {
        var typeGotEmitted = false
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                if (emissionChecker.check(o)) {
                    typeGotEmitted = true
                    latch.countDown()
                    this@isValueGetsEmitted.removeObserver(this)
                }
            }
        }

        this.observeForever(observer)

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            return typeGotEmitted
        }

        @Suppress("UNCHECKED_CAST")
        return typeGotEmitted
    }

    interface EmissionChecker {
        fun <T> check(value: T?): Boolean
    }

}
