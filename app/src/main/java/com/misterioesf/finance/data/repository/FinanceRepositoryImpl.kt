package com.misterioesf.finance.data.repository

import androidx.lifecycle.LiveData
import com.misterioesf.finance.data.dao.FinanceDao
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.data.dao.entity.Transfer
import com.misterioesf.finance.domain.data.FinanceRepository
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinanceRepositoryImpl @Inject constructor(val dao: FinanceDao): FinanceRepository {
    private val executor = Executors.newSingleThreadExecutor()

    override suspend fun getAllTransfers(): Flow<List<Transfer>> {
        return dao.getAllTransfers()
    }

    override suspend fun getAllTransfersByAccount(accountId: Int): Flow<List<Transfer>> {
        return dao.getAllTransfersByAccountId(accountId)
    }

    override fun getAccountAmountById(accountId: Int): Double {
        return dao.getAccountAmountById(accountId)
    }

    override fun getAccountBillAmountById(accountId: Int): Double {
        return dao.getAccountBillAmountById(accountId)
    }

    override fun getAllAccounts(): LiveData<List<Account>> {
        return dao.getAllAccounts()
    }

    override suspend fun getAllAccountsList(): Flow<List<Account>> {
        return dao.getAllAccountsList()
    }

    override suspend fun getAccountById(accountId: Int): Flow<Account> {
        return dao.getAccountById(accountId)
    }

    override suspend fun getTransferById(transferId: Int): Flow<Transfer> {
        return dao.getTransferById(transferId)
    }

    override suspend fun addNewTransfer(transfer: Transfer) {
        executor.execute {
            dao.addNewTransfer(transfer)
        }
    }

    override fun addNewAccount(account: Account) {
        executor.execute {
            dao.addNewAccount(account)
        }
    }

    override suspend fun updateAccount(account: Account) {
        dao.updateAccount(account)
    }

    override suspend fun updateTransfer(transfer: Transfer) {
        dao.updateTransfer(transfer)
    }

    override suspend fun deleteTransfer(transfer: Transfer) {
        dao.deleteTransfer(transfer)
    }

    override suspend fun deleteAccount(account: Account) {
        dao.deleteAccount(account)
    }
}