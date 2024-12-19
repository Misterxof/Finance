package com.misterioesf.finance.domain.data

import androidx.lifecycle.LiveData
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.data.dao.entity.Transfer
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {
    suspend fun getAllTransfers(): Flow<List<Transfer>>

    suspend fun getAllTransfersByAccount(accountId: Int): Flow<List<Transfer>>

    fun getAccountAmountById(accountId: Int): Double

    fun getAccountBillAmountById(accountId: Int): Double

    fun getAllAccounts(): LiveData<List<Account>>

    suspend fun getAccountById(accountId: Int): Flow<Account>

    suspend fun getTransferById(transferId: Int): Flow<Transfer>

    suspend fun getAllAccountsList(): Flow<List<Account>>

    suspend fun addNewTransfer(transfer: Transfer)

    fun addNewAccount(account: Account)

    suspend fun updateAccount(account: Account)

    suspend fun updateTransfer(transfer: Transfer)

    suspend fun deleteTransfer(transfer: Transfer)

    suspend fun deleteAccount(account: Account)
}