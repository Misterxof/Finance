package com.misterioesf.finance.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.ColorSetter
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.data.entity.Course
import com.misterioesf.finance.data.entity.Currencies
import com.misterioesf.finance.repository.CurrencyRepository
import com.misterioesf.finance.repository.FinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository,
    private val repo: FinanceRepository
) : BaseViewModel(repo) {
    private val accountTreeMap = TreeMap<String, Account>()  // * msf + stateFlow
    private val _usdCourse = MutableStateFlow<Course?>(null)
    private val _eurCourse = MutableStateFlow<Course?>(null)

    private var usdTotalAmount = 0.0
    private var eurTotalAmount = 0.0
    private var bynTotalAmount = 0.0
    private var usdRate = 1f
    private var eurRate = 1f
    private var isAccountsAreSet = false
    private var isUsdCurrencyAreSet = false
    private var isEurCurrencyAreSet = false

    val usdCourse = _usdCourse.asStateFlow()
    val eurCourse = _eurCourse.asStateFlow()

    init {
        getUSDCourse()
        getEURCourse()
    }

    private fun getUSDCourse() {
        viewModelScope.launch {
            try {
                currencyRepository.getUSDCourse().collect() {
                    _usdCourse.value = it
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", e.stackTraceToString())
            }
        }
    }

    private fun getEURCourse() {
        viewModelScope.launch {
            try {
                currencyRepository.getEURCourse().collect() {
                    _eurCourse.value = it
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", e.stackTraceToString())
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
            setUsdAmount(true)
            setEurAmount(true)

            accountTreeMap
        }
    }

    fun setUsdAmount(rate: Float) {
        isUsdCurrencyAreSet = true
        this.usdRate = rate
        if (isAccountsAreSet && isUsdCurrencyAreSet && isEurCurrencyAreSet)
            setAllTotalAmounts()
    }

    private fun setUsdAmount(isAccount: Boolean) {
        isAccountsAreSet = true
        if (isAccountsAreSet && isUsdCurrencyAreSet && isEurCurrencyAreSet)
            setAllTotalAmounts()
    }

    fun setEurAmount(rate: Float) {
        isEurCurrencyAreSet = true
        this.eurRate = rate
        if (isAccountsAreSet && isUsdCurrencyAreSet && isEurCurrencyAreSet)
            setAllTotalAmounts()
    }

    private fun setEurAmount(isAccount: Boolean) {
        isAccountsAreSet = isAccount
        if (isAccountsAreSet && isUsdCurrencyAreSet && isEurCurrencyAreSet)
            setAllTotalAmounts()
    }

    private fun setAllTotalAmounts() {
        eurTotalAmount = 0.0
        usdTotalAmount = 0.0
        bynTotalAmount = 0.0
        accountTreeMap.forEach { account ->
            when (Currencies.valueOf(account.value.currency)) {
                Currencies.USD -> {
                    eurTotalAmount += account.value.sum * usdRate / eurRate
                    usdTotalAmount += account.value.sum
                    bynTotalAmount += account.value.sum * usdRate
                }
                Currencies.BYN -> {
                    eurTotalAmount += account.value.sum / eurRate
                    usdTotalAmount += account.value.sum / usdRate
                    bynTotalAmount += account.value.sum
                }
                Currencies.EU -> {
                    eurTotalAmount += account.value.sum
                    usdTotalAmount += account.value.sum * eurRate / usdRate
                    bynTotalAmount += account.value.sum * eurRate
                }
                else -> {}
            }
        }
    }

    fun getAmountMap(): TreeMap<String, Account> {
        return accountTreeMap
    }

    fun getUsdTotalAmount(): Double {
        isUsdCurrencyAreSet = false
        return usdTotalAmount
    }

    fun getEurTotalAmount(): Double {
        isAccountsAreSet = false
        isEurCurrencyAreSet = false
        return eurTotalAmount
    }

    fun getBynTotalAmount(): Double {
        return bynTotalAmount
    }
}