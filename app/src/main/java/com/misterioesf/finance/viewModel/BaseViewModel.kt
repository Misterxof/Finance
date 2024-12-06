package com.misterioesf.finance.viewModel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(protected val repository: FinanceRepository): ViewModel() {
    fun getAccountList(): LiveData<List<Account>> {
        return repository.getAllAccounts()
    }

    suspend fun setAccountAmount(accountId: Int): Double {
        return withContext(Dispatchers.Default) {
            val billAmount = getBillAmount(accountId)
            (repository.getAccountAmountById(accountId) - billAmount)
        }
    }

    suspend fun getBillAmount(accountId: Int): Double {
        return withContext(Dispatchers.Default) {
            repository.getAccountBillAmountById(accountId)
        }
    }
}