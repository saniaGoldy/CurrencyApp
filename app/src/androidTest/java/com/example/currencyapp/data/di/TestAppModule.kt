package com.example.currencyapp.data.di

import android.content.Context
import androidx.room.Room
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.domain.services.NetworkConnectivityObserver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDB(@ApplicationContext context: Context) = Room.inMemoryDatabaseBuilder(
        context,
        LocalDB::class.java
    ).allowMainThreadQueries().build()
}