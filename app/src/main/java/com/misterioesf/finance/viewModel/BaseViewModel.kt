package com.misterioesf.finance.viewModel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel() {
    protected val repository = FinanceRepository.getFinanceRepository()

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