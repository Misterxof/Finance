package com.misterioesf.finance.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.dao.entity.Transfer
import com.misterioesf.finance.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransferViewModel: ViewModel() {
    private val financeRepository = FinanceRepository.getFinanceRepository()

    fun addNewTransfer(transfer: Transfer) {
        financeRepository.addNewTransfer(transfer)
    }

    fun deleteTransfer(transfer: Transfer) {
        viewModelScope.launch(Dispatchers.Default) {
            financeRepository.deleteTransfer(transfer)
        }
    }
}