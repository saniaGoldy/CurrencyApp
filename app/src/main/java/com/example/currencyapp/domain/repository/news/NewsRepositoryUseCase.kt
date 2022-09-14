package com.example.currencyapp.domain.repository.news

import com.example.currencyapp.data.remote.entities.news.Data
import com.example.currencyapp.data.remote.entities.news.SearchSettings
import com.example.currencyapp.data.repository.preferences.PreferencesRepository
import com.example.currencyapp.data.repository.remote.RemoteRepository
import com.example.currencyapp.domain.model.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val remoteRepository: RemoteRepository
) : NewsRepository {

    override suspend fun fetchNewsList(settings: SearchSettings): DataState<List<Data>> {
        return remoteRepository.fetchNewsList(settings)
    }

    override fun loadNewsSettings(): Flow<SearchSettings> =
        preferencesRepository.loadNewsSettings()

    override fun saveNewsSettings(settings: SearchSettings, scope: CoroutineScope) =
        preferencesRepository.saveNewsSettings(settings, scope)
}