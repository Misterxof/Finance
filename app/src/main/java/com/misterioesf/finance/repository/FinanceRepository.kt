package com.misterioesf.finance.repository

import androidx.lifecycle.LiveData
import com.misterioesf.finance.dao.FinanceDao
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.dao.entity.Transfer
import java.util.concurrent.Executors

class FinanceRepository(val dao: FinanceDao) {
    private val executor = Executors.newSingleThreadExecutor()

    fun getAllTransfers(): LiveData<List<Transfer>> {
        return dao.getAllTransfers()
    }

    fun getAllTransfersByAccount(accountId: Int): LiveData<List<Transfer>> {
        return dao.getAllTransfersByAccountId(accountId)
    }

    fun getAccountAmountById(accountId: Int): Double {
        return dao.getAccountAmountById(accountId)
    }

    fun getAccountBillAmountById(accountId: Int): Double {
        return dao.getAccountBillAmountById(accountId)
    }

    fun getAllAccounts(): LiveData<List<Account>> {
        return dao.getAllAccounts()
    }

    fun getAllAccountsList(): List<Account> {
        return dao.getAllAccountsList()
    }

    fun addNewTransfer(transfer: Transfer) {
        executor.execute {
            dao.addNewTransfer(transfer)
        }
    }

    fun addNewAccount(account: Account) {
        executor.execute {
            dao.addNewAccount(account)
        }
    }

    suspend fun deleteTransfer(transfer: Transfer) {
        dao.deleteTransfer(transfer)
    }

    suspend fun deleteAccount(account: Account) {
        dao.deleteAccount(account)
    }

    companion object {
        private var repository: FinanceRepository? = null

        fun initialize(dao: FinanceDao) {
            if (repository == null) repository = FinanceRepository(dao)
        }

        fun getFinanceRepository(): FinanceRepository {
            return repository ?: throw java.lang.IllegalStateException("FinanceRepository must be init")
        }
    }
}