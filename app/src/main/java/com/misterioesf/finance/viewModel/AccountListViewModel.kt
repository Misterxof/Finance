package com.misterioesf.finance.viewModel

import androidx.lifecycle.ViewModel
import com.misterioesf.finance.data.repository.FinanceRepositoryImpl
import com.misterioesf.finance.domain.usecase.AccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountListViewModel @Inject constructor(private val accountUseCase: AccountUseCase): ViewModel() {
    val allAccounts = accountUseCase.allAccounts
}