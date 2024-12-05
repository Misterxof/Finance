package com.misterioesf.finance.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.ColorSetter
import com.misterioesf.finance.Currencies
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.dao.entity.Course
import com.misterioesf.finance.repository.CurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeViewModel : BaseViewModel() {
    private val accountTreeMap = TreeMap<String, Account>()  // * msf + stateFlow
    private val currencyRepository = CurrencyRepository.getCurrencyRepository()
    private val _currencyCourse = MutableStateFlow<Course?>(null)

    private var totalAmount = 0.0
    private var rate = 0f
    private var totalAmountCurrency = Currencies.USD
    private var isAccountsAreSet = false
    private var isCurrencyAreSet = false

    val currencyCourse = _currencyCourse.asStateFlow()

    init {
        getCurrencyCourse()
    }

    private fun getCurrencyCourse() {
        viewModelScope.launch {
            try {
                currencyRepository.getCurrencyCourse().collect() {
                    _currencyCourse.value = it
                }
            } catch (e: Exception) {
                Log.e("WWWWWWWWWWWWWW", e.stackTraceToString())
            }
        }
    }

    suspend fun getTreeMap(): TreeMap<String, Account> {
        accountTreeMap.clear()

        return withContext(Dispatchers.IO) {
            val accountsList = withContext(Dispatchers.IO) { repository.getAllAccountsList() }

            accountsList?.let { list ->
                ColorSetter.clear()
                list.forEach { account ->
                    val amount = setAccountAmount(accountId = account.id)
                    accountTreeMap[account.name] = account
                    account.sum = amount
                    account.color = ColorSetter.getNextColor()
                }
            }
            setAmount(true)

            accountTreeMap
        }
    }

    // it too long to wait currency api call, init with sp value and then just update it
    fun setAmount(rate: Float) {
        isCurrencyAreSet = true
        this.rate = rate
        if (isAccountsAreSet && isCurrencyAreSet)
            setTotalAmount()
    }

    private fun setAmount(isAccount: Boolean) {
        isAccountsAreSet = isAccount
        if (isAccountsAreSet && isCurrencyAreSet)
            setTotalAmount()
    }

    private fun setTotalAmount() {
        totalAmount = 0.0
        accountTreeMap.forEach { account ->
            when (Currencies.valueOf(account.value.currency)) {
                Currencies.USD -> totalAmount += account.value.sum
                Currencies.BYN -> totalAmount += account.value.sum / rate
                else -> {}
            }
        }
    }

    fun getAmountMap(): TreeMap<String, Account> {
        return accountTreeMap
    }

    fun getTotalAmount(): Double {
        isAccountsAreSet = false
        isCurrencyAreSet = false
        return totalAmount
    }
}