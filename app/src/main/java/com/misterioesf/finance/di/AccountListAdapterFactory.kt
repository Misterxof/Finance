package com.misterioesf.finance.di

import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.ui.adapter.AccountListAdapter
import dagger.assisted.AssistedFactory

@AssistedFactory
interface AccountListAdapterFactory {
    fun create(
        accountList: List<Account>,
        itemViewId: Int,
        onClick: (account: Account?) -> Unit
    ): AccountListAdapter
}