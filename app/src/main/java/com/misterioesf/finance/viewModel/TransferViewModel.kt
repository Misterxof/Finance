package com.misterioesf.finance.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.data.dao.entity.Transfer
import com.misterioesf.finance.data.repository.FinanceRepositoryImpl
import com.misterioesf.finance.domain.usecase.TransferUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(private val transferUseCase: TransferUseCase): ViewModel() {
    fun addNewTransfer(transfer: Transfer) {
        viewModelScope.launch(Dispatchers.IO) {
            transferUseCase.addNewTransfer(transfer)
        }
    }

    fun deleteTransfer(transfer: Transfer) {
        viewModelScope.launch(Dispatchers.IO) {
            transferUseCase.deleteTransfer(transfer)
        }
    }
}