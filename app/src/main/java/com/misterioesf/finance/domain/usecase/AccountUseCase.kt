package com.misterioesf.finance.domain.usecase

import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.domain.data.FinanceRepository
import com.misterioesf.finance.domain.model.StatefulData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

open class AccountUseCase @Inject constructor(
    private val financeRepository: FinanceRepository
) {
    protected val _allAccounts =
        MutableStateFlow<StatefulData<List<Account>>>(StatefulData.LoadingState(emptyList()))

    val allAccounts = _allAccounts.asStateFlow()

    init {
        getAllAccounts()
    }

    private fun getAllAccounts() {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            financeRepository.getAllAccountsList().collect {
                _allAccounts.emit(StatefulData.SuccessState(it))
            }
        }
    }
}