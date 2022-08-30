package com.example.currencyapp.di

import com.example.currencyapp.data.repository.MainRepositoryImpl
import com.example.currencyapp.domain.repository.MainRepository
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
    abstract fun bindMyRepository(
        myRepositoryImpl: MainRepositoryImpl
    ): MainRepository
}