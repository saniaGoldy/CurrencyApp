package com.example.currencyapp.ui.news.webPageActivity

import android.app.Application
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    context: Application
) : BaseViewModel(context)