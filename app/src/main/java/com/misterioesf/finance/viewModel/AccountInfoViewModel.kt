package com.misterioesf.finance.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.dao.entity.Transfer
import com.misterioesf.finance.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountInfoViewModel @Inject constructor(private val financeRepository: FinanceRepository) :
    ViewModel() {
    private var accountId = -1
    private var account: Account? = null

    var transfersList: List<Transfer> = emptyList()


    fun getTransfersById(accountId: Int): LiveData<List<Transfer>> {
        return financeRepository.getAllTransfersByAccount(accountId)
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch(Dispatchers.Default) { financeRepository.deleteAccount(account) }
    }

    fun getAccountId() = accountId

    fun setAccountId(value: Int) {
        accountId = value
    }

    fun getTransfersList(position: Int): List<Transfer> {
        when (position) {
            0 -> return transfersList ?: emptyList()
            1 -> {
                transfersList?.let { transferList ->
                    return transferList.filter { !it.isBill }
                }
            }
            2 -> {
                transfersList?.let { transferList ->
                    return transferList.filter { it.isBill }
                }
            }
        }
        return emptyList()
    }

    fun getAccount() = account

    fun setAccount(account: Account) {
        this.account = account
    }
}