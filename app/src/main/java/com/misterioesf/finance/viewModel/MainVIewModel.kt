package com.misterioesf.finance.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.CurrencyService
import com.misterioesf.finance.dao.entity.Course
import com.misterioesf.finance.repository.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainVIewModel() : ViewModel() {

}