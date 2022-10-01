package com.example.currencyapp.ui.utils

import android.widget.SearchView

class MyOnQueryTextListener(private val queryFilter: QueryFilter): SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
        queryFilter.filter(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        queryFilter.filter(newText)
        return true
    }

    interface QueryFilter{
        fun filter(keyword: String?)
    }
}