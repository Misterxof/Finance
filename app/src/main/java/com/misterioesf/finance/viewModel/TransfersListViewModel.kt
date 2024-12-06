package com.misterioesf.finance.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.Currencies
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.dao.entity.Transfer
import com.misterioesf.finance.repository.FinanceRepository
import kotlinx.coroutines.*

class TransfersListViewModel : ViewModel() {
    private val financeRepository = FinanceRepository.getFinanceRepository()
    private var accountId: Int = -1
    private var currency = Currencies.BYN

    val transfers = financeRepository.getAllTransfers()

    fun getTransfersById(accountId: Int): LiveData<List<Transfer>> {
       return financeRepository.getAllTransfersByAccount(accountId)
    }

    suspend fun getAllAccounts(): List<Account> {
        return withContext(Dispatchers.Default){
            financeRepository.getAllAccountsList()
        }
    }

    fun getAccountIndexById(id: Int, accountsList: List<Account>?) : Int {
        accountsList?.forEachIndexed { i, acc ->
            if (acc.id == id) return i
        }
        return 0
    }

    fun getAccountIdByName(name: String, accountsList: List<Account>?): Int? {
        if (!accountsList.isNullOrEmpty()) {
            accountsList!!.forEach {
                if (it.name == name) {
                    setCurrency(Currencies.valueOf(it.currency))
                    return it.id
                }
            }
        }

        return null
    }

    fun getAccountId() = accountId
    fun setAccountId(value: Int) { accountId = value }

    fun getCurrency() = currency
    fun setCurrency(value: Currencies)  { currency = value}
}