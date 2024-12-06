package com.misterioesf.finance.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountHomeViewModel @Inject constructor(private val repository: FinanceRepository): ViewModel() {
    fun addNewAccount(account: Account) {
        repository.addNewAccount(account)
    }

    fun getAccountAmount(accountId: Int): Double? {
        return repository.getAllTransfersByAccount(accountId).value?.sumOf { it.sum }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch(Dispatchers.Default) { repository.deleteAccount(account) }
    }
}