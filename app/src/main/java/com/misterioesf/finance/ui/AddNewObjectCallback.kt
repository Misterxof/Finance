package com.misterioesf.finance.ui

import com.misterioesf.finance.Currencies
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.dao.entity.Transfer

interface AddNewObjectCallback {
    fun onTransferSelected(accountId: Int, currency: Currencies, transfer: Transfer?)
    fun onAccountSelected(account: Account?)
}