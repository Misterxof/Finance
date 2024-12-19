package com.misterioesf.finance.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.data.dao.entity.Transfer
import com.misterioesf.finance.data.repository.FinanceRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountHomeViewModel @Inject constructor(private val repository: FinanceRepositoryImpl) :
    ViewModel() {
    fun addNewAccount(account: Account) {
        repository.addNewAccount(account)
    }
}