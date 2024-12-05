package com.misterioesf.finance.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.dao.entity.Transfer

@Dao
interface FinanceDao {
    @Query("SELECT * FROM transfer")
    fun getAllTransfers(): LiveData<List<Transfer>>

    @Query("SELECT * FROM transfer WHERE account_id = :accountId")
    fun getAllTransfersByAccountId(accountId: Int): LiveData<List<Transfer>>

    @Query("SELECT SUM(sum) FROM transfer WHERE account_id = :accountId AND is_bill = 0")
    fun getAccountAmountById(accountId: Int): Double

    @Query("SELECT SUM(sum) FROM transfer WHERE account_id = :accountId AND is_bill = 1")
    fun getAccountBillAmountById(accountId: Int): Double

    @Query("SELECT * FROM account")
    fun getAllAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM account")
    fun getAllAccountsList(): List<Account>

    @Insert
    fun addNewTransfer(transfer: Transfer)

    @Insert
    fun addNewAccount(account: Account)

    @Delete
    fun deleteTransfer(transfer: Transfer)

    @Delete
    fun deleteAccount(account: Account)
}