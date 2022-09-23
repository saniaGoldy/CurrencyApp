package com.example.currencyapp.data.di

import android.app.Application
import androidx.room.Room
import com.example.currencyapp.data.data_source.local.LocalDBDataSourceImpl
import com.example.currencyapp.data.data_source.preferences.PreferencesDataSourceImpl
import com.example.currencyapp.data.data_source.remote.RemoteDataSourceImpl
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.dataStore
import com.example.currencyapp.other.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCurrencyAPI(): CurrencyAPI {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val clientBuilder = OkHttpClient.Builder().addInterceptor(interceptor)
        return Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideLocalDB(context: Application): LocalDB {
        return Room.databaseBuilder(
            context.applicationContext,
            LocalDB::class.java,
            Constants.CURRENCY_LOCAL_DB_NAME
        ).fallbackToDestructiveMigration().build()
    }


    @Provides
    @Singleton
    fun providePreferencesRepository(context: Application): PreferencesDataSourceImpl {
        return PreferencesDataSourceImpl(context.dataStore)
    }

    @Provides
    @Singleton
    fun provideLocalRepository(localDB: LocalDB): LocalDBDataSourceImpl {
        return LocalDBDataSourceImpl(localDB)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(currencyAPI: CurrencyAPI): RemoteDataSourceImpl {
        return RemoteDataSourceImpl(currencyAPI)
    }
}