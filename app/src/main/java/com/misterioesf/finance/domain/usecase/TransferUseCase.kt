package com.misterioesf.finance.domain.usecase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.data.dao.entity.Transfer
import com.misterioesf.finance.domain.data.FinanceRepository
import com.misterioesf.finance.domain.model.StatefulData
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransferUseCase @Inject constructor(private val financeRepository: FinanceRepository) {

    suspend fun addNewTransfer(transfer: Transfer) {
        financeRepository.addNewTransfer(transfer)
        updateAccountInfo(transfer.accountId, transfer.sum, transfer.isBill)
    }

    private suspend fun updateAccountInfo(accountId: Int, amount: Double, isBill: Boolean) {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            val account = getAccountById(accountId).first()
            if (isBill) account.sum -= amount
            else account.sum += amount
            updateAccount(account)
        }
    }

    suspend fun getAccountById(accountId: Int): Flow<Account> {
        return financeRepository.getAccountById(accountId)
    }

    suspend fun updateAccount(account: Account) {
        financeRepository.updateAccount(account)
    }

    suspend fun getTransferById(transferId: Int): Flow<Transfer> {
        return financeRepository.getTransferById(transferId)
    }

    suspend fun updateTransfer(transfer: Transfer) {
        financeRepository.updateTransfer(transfer)
    }

    suspend fun deleteTransfer(transfer: Transfer) {
        financeRepository.deleteTransfer(transfer)
        updateAccountInfo(transfer.accountId, transfer.sum, !transfer.isBill)
    }

    suspend fun getAllTransfers(): Flow<List<Transfer>> {
        return financeRepository.getAllTransfers()
    }

    suspend fun getTransfersById(accountId: Int): Flow<List<Transfer>> {
        return financeRepository.getAllTransfersByAccount(accountId)
    }
}