package com.misterioesf.finance.domain.usecase

import com.misterioesf.finance.data.dao.entity.Transfer
import com.misterioesf.finance.domain.data.FinanceRepository
import com.misterioesf.finance.domain.model.StatefulData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransferListUseCase @Inject constructor(private val financeRepository: FinanceRepository): AccountUseCase(financeRepository) {
    private val _transfersListUiState = MutableStateFlow<StatefulData<List<Transfer>>>(
        StatefulData.LoadingState(emptyList())
    )

    val transfersListUiState = _transfersListUiState.asStateFlow()

    init {
        getAllTransfers()
    }

    private fun getAllTransfers() {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            financeRepository.getAllTransfers().collect {
                _transfersListUiState.value = StatefulData.SuccessState(it)
            }
        }
    }

    fun getTransfersById(accountId: Int) {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            financeRepository.getAllTransfersByAccount(accountId).collect {
                _transfersListUiState.value = StatefulData.SuccessState(it)
            }
        }
    }
}