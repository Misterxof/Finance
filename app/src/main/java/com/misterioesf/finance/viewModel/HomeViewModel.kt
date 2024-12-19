package com.misterioesf.finance.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.ColorSetter
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.domain.model.Course
import com.misterioesf.finance.domain.model.Currencies
import com.misterioesf.finance.data.repository.CurrencyRepositoryImpl
import com.misterioesf.finance.data.repository.FinanceRepositoryImpl
import com.misterioesf.finance.domain.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
): ViewModel() {
    val allAccounts = homeUseCase.allAccounts
    val allCourses = homeUseCase.allCourses
    val totalAmounts = homeUseCase.totalAmounts
}