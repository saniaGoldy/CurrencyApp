package com.example.currencyapp.ui.mainActivity

import android.app.Application
import com.example.currencyapp.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(context: Application) : BaseViewModel(context)