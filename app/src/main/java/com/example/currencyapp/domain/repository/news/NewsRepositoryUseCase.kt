package com.example.currencyapp.domain.repository.news

import android.app.Application
import com.example.currencyapp.data.local.LocalDB
import com.example.currencyapp.data.remote.CurrencyAPI
import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.data.repository.LocalDBRepository
import com.example.currencyapp.data.repository.PreferencesRepository
import com.example.currencyapp.data.repository.RemoteRepository
import com.example.currencyapp.domain.model.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryUseCase @Inject constructor(
    localDB: LocalDB,
    context: Application,
    currencyAPI: CurrencyAPI
) : NewsRepository {

    private val localDBRepository = LocalDBRepository(localDB)
    private val preferencesRepository = PreferencesRepository(context)
    private val remoteRepository = RemoteRepository(currencyAPI)

    override suspend fun fetchNewsList(settings: SearchSettings): DataState<List<Data>> {
        return remoteRepository.fetchNewsList(settings)
    }

    override fun loadNewsSettings(): Flow<SearchSettings> =
        preferencesRepository.loadNewsSettings()

    override fun saveNewsSettings(settings: SearchSettings, scope: CoroutineScope) =
        preferencesRepository.saveNewsSettings(settings, scope)
}