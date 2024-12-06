package com.misterioesf.finance.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.misterioesf.finance.data.entity.Currencies
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.dao.entity.Transfer
import com.misterioesf.finance.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class TransfersListViewModel @Inject constructor(
    private val financeRepository: FinanceRepository
) : ViewModel() {
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