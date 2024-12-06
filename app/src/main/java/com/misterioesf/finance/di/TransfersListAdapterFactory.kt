package com.misterioesf.finance.di

import com.misterioesf.finance.dao.entity.Transfer
import com.misterioesf.finance.ui.adapter.TransfersListAdapter
import dagger.assisted.AssistedFactory

@AssistedFactory
interface TransfersListAdapterFactory {

    fun create(
        transfersList: List<Transfer>,
        onTransferClick: (transfer: Transfer) -> Unit
    ): TransfersListAdapter
}