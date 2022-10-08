package com.example.currencyapp.data.di

import com.example.currencyapp.data.data_source.local.LocalDBDataSource
import com.example.currencyapp.data.data_source.local.LocalDBDataSourceImpl
import com.example.currencyapp.data.data_source.preferences.PreferencesDataSource
import com.example.currencyapp.data.data_source.preferences.PreferencesDataSourceImpl
import com.example.currencyapp.data.data_source.remote.RemoteDataSource
import com.example.currencyapp.data.data_source.remote.RemoteDataSourceImpl
import com.example.currencyapp.data.repository.NewsRepositoryImpl
import com.example.currencyapp.data.repository.RatesRepositoryImpl
import com.example.currencyapp.domain.repository.NewsRepository
import com.example.currencyapp.domain.repository.RatesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRatesRepository(
        myRepositoryImpl: RatesRepositoryImpl
    ): RatesRepository

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        myRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository

    @Binds
    @Singleton
    abstract fun bindLocalRepository(
        myRepositoryImpl: LocalDBDataSourceImpl
    ): LocalDBDataSource

    @Binds
    @Singleton
    abstract fun bindPrefRepository(
        myRepositoryImpl: PreferencesDataSourceImpl
    ): PreferencesDataSource

    @Binds
    @Singleton
    abstract fun bindRemoteRepository(
        myRepositoryImpl: RemoteDataSourceImpl
    ): RemoteDataSource
}