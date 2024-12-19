package com.misterioesf.finance.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.data.dao.entity.Transfer
import com.misterioesf.finance.data.repository.FinanceRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountInfoViewModel @Inject constructor(private val financeRepositoryImpl: FinanceRepositoryImpl) :
    ViewModel() {
    private var accountId = -1
    private var account: Account? = null

    var transfersList: List<Transfer> = emptyList()


    suspend fun getTransfersById(accountId: Int): LiveData<List<Transfer>> {
        return financeRepositoryImpl.getAllTransfersByAccount(accountId).asLiveData()
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch(Dispatchers.Default) { financeRepositoryImpl.deleteAccount(account) }
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