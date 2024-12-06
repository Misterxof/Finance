package com.misterioesf.finance.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.dao.entity.Transfer
import com.misterioesf.finance.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(private val financeRepository: FinanceRepository): ViewModel() {
    fun addNewTransfer(transfer: Transfer) {
        financeRepository.addNewTransfer(transfer)
    }

    fun deleteTransfer(transfer: Transfer) {
        viewModelScope.launch(Dispatchers.Default) {
            financeRepository.deleteTransfer(transfer)
        }
    }
}