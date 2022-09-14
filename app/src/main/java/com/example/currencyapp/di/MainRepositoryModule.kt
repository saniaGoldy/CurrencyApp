package com.example.currencyapp.di

import com.example.currencyapp.data.repository.local.LocalDBRepository
import com.example.currencyapp.data.repository.local.LocalDBRepositoryImpl
import com.example.currencyapp.data.repository.preferences.PreferencesRepository
import com.example.currencyapp.data.repository.preferences.PreferencesRepositoryImpl
import com.example.currencyapp.data.repository.remote.RemoteRepository
import com.example.currencyapp.data.repository.remote.RemoteRepositoryImpl
import com.example.currencyapp.domain.repository.news.NewsRepository
import com.example.currencyapp.domain.repository.news.NewsRepositoryUseCase
import com.example.currencyapp.domain.repository.rates.RatesRepository
import com.example.currencyapp.domain.repository.rates.RatesRepositoryUseCase
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
        myRepositoryImpl: RatesRepositoryUseCase
    ): RatesRepository

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        myRepositoryImpl: NewsRepositoryUseCase
    ): NewsRepository

    @Binds
    @Singleton
    abstract fun bindLocalRepository(
        myRepositoryImpl: LocalDBRepositoryImpl
    ): LocalDBRepository

    @Binds
    @Singleton
    abstract fun bindPrefRepository(
        myRepositoryImpl: PreferencesRepositoryImpl
    ): PreferencesRepository

    @Binds
    @Singleton
    abstract fun bindRemoteRepository(
        myRepositoryImpl: RemoteRepositoryImpl
    ): RemoteRepository
}