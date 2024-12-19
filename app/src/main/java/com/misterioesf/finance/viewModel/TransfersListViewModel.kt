package com.misterioesf.finance.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.domain.model.Currencies
import com.misterioesf.finance.domain.model.StatefulData.SuccessState
import com.misterioesf.finance.domain.usecase.TransferListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransfersListViewModel @Inject constructor(
    private val transferListUseCase: TransferListUseCase
) : ViewModel() {
    private var accountId: Int = -1
    private var currency = Currencies.BYN

    val allAccounts = transferListUseCase.allAccounts
    val transfersListUiState = transferListUseCase.transfersListUiState

    init {
        getAllAccounts()
    }

    private fun getTransfersById(accountId: Int) {
        this.accountId = accountId
        transferListUseCase.getTransfersById(accountId)
    }

    fun getAllAccounts() {
        viewModelScope.launch(Dispatchers.IO) {
            transferListUseCase.allAccounts.collect {
                if (it is SuccessState) {
                    currency = Currencies.valueOf(it.data!![0].currency)
                    getTransfersById(it.data!![0].id)
                }
            }
        }
    }

    fun getNewAccountById(position: Int) {
        val account = transferListUseCase.allAccounts.value.data!![position]
        currency = Currencies.valueOf(account.currency)
        getTransfersById(account.id)
    }

    fun getAccountIndexById(): Int {
        if (transfersListUiState.value is SuccessState) {
            transfersListUiState.value?.data?.forEachIndexed { i, acc ->
                if (acc.id == accountId) return i
            }
        }

        return 0
    }

    fun getAccountId() = accountId

    fun getCurrency() = currency
    fun setCurrency(value: Currencies) {
        currency = value
    }
}