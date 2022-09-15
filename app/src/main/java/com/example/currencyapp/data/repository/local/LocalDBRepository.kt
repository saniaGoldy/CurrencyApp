package com.example.currencyapp.data.repository.local

import com.example.currencyapp.domain.model.CurrencyData
import com.example.currencyapp.domain.model.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

interface LocalDBRepository {
    /**TODO: It has no sense to return DataState from the repository.
     * As repository doesn't know anything about the loading state, or default.
     * It can be either Success or error.Your repository should return just data.
     * UseCase wrap repository and return result. You can use kotlin's result to return from the usecase.
     * And ViewModel should provide DataState(which should be renamed to a ViewState) to the fragment
     */
    suspend fun fetchCurrenciesList(): DataState<List<CurrencyData>>

    suspend fun saveCurrenciesList(currencies: List<CurrencyData>)

    suspend fun updateCurrenciesList(currencies: List<CurrencyData>)
}