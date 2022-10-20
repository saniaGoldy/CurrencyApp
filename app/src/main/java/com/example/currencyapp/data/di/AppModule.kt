package com.example.currencyapp.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.currencyapp.data.data_source.local.LocalDBDataSourceImpl
import com.example.currencyapp.data.data_source.mappers.currencies.CurrenciesRateStoryMapper
import com.example.currencyapp.data.data_source.mappers.currencies.DataToEntityMapper
import com.example.currencyapp.data.data_source.mappers.currencies.EntityToDataMapper
import com.example.currencyapp.data.data_source.mappers.news.NewsDataMapper
import com.example.currencyapp.data.data_source.preferences.PreferencesDataSourceImpl
import com.example.currencyapp.data.data_source.remote.RemoteDataSourceImpl
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.dataStore
import com.example.currencyapp.domain.services.ConnectivityObserver
import com.example.currencyapp.domain.services.NetworkConnectivityObserver
import com.example.currencyapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCurrencyAPI(clientBuilder: OkHttpClient.Builder): CurrencyAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideClientBuilder(@ApplicationContext context: Context): OkHttpClient.Builder {

        val interceptor = ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                val request = chain
                    .request()
                    .newBuilder()
                    .addHeader("apikey", "jwTY3ePdZwCZrZ1kP96pLfnUe9qUpOq9")
                    .build()
                chain.proceed(request)
            }
    }

    @Provides
    @Singleton
    fun provideLocalDB(context: Application): LocalDB =
        Room.databaseBuilder(
            context.applicationContext,
            LocalDB::class.java,
            Constants.CURRENCY_LOCAL_DB_NAME
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providePreferencesRepository(context: Application): PreferencesDataSourceImpl =
        PreferencesDataSourceImpl(context.dataStore)

    @Provides
    @Singleton
    fun provideLocalRepository(localDB: LocalDB): LocalDBDataSourceImpl =
        LocalDBDataSourceImpl(localDB, EntityToDataMapper(), DataToEntityMapper())

    @Provides
    @Singleton
    fun provideRemoteRepository(currencyAPI: CurrencyAPI): RemoteDataSourceImpl =
        RemoteDataSourceImpl(currencyAPI, NewsDataMapper(), CurrenciesRateStoryMapper())

    @Provides
    @Singleton
    fun provideNetworkConnectivityObserver(@ApplicationContext context: Context): NetworkConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}