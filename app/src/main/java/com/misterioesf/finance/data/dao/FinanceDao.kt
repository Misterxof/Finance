package com.misterioesf.finance.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.data.dao.entity.Transfer
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {
    @Query("SELECT * FROM transfer")
    fun getAllTransfers(): Flow<List<Transfer>>

    @Query("SELECT * FROM transfer WHERE account_id = :accountId")
    fun getAllTransfersByAccountId(accountId: Int): Flow<List<Transfer>>

    @Query("SELECT SUM(sum) FROM transfer WHERE account_id = :accountId AND is_bill = 0")
    fun getAccountAmountById(accountId: Int): Double

    @Query("SELECT SUM(sum) FROM transfer WHERE account_id = :accountId AND is_bill = 1")
    fun getAccountBillAmountById(accountId: Int): Double

    @Query("SELECT * FROM account")
    fun getAllAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM account")
    fun getAllAccountsList(): Flow<List<Account>>

    @Query("SELECT * FROM account WHERE id = :accountId")
    fun getAccountById(accountId: Int): Flow<Account>

    @Query("SELECT * FROM transfer WHERE id = :transferId")
    fun getTransferById(transferId: Int): Flow<Transfer>

    @Insert
    fun addNewTransfer(transfer: Transfer)

    @Insert
    fun addNewAccount(account: Account)

    @Update
    fun updateAccount(account: Account)

    @Update
    fun updateTransfer(transfer: Transfer)

    @Delete
    fun deleteTransfer(transfer: Transfer)

    @Delete
    fun deleteAccount(account: Account)
}