package com.misterioesf.finance.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.ColorSetter
import com.misterioesf.finance.Currencies
import com.misterioesf.finance.dao.entity.Account
import com.misterioesf.finance.dao.entity.Course
import com.misterioesf.finance.repository.CurrencyRepository
import com.misterioesf.finance.repository.FinanceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class HomeViewModel : BaseViewModel() {
    private val accountTreeMap: TreeMap<String, Account> = TreeMap<String, Account>()
    private val allAccountList = mutableListOf<Account>()
    private val currencyRepository = CurrencyRepository.getCurrencyRepository()
    private val _currencyCourse = MutableStateFlow<Course?>(null)
    private var totalAmount = 0.0
    private var totalAmountCurrency = Currencies.USD

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
                    allAccountList.add(account)
                }
            }

            accountTreeMap
        }
    }

    suspend fun setAmount(rate: Float) {
        totalAmount = 0.0
        allAccountList.forEach {account ->
            when(Currencies.valueOf(account.currency)) {
                Currencies.USD -> totalAmount += account.sum
                Currencies.BYN -> totalAmount += account.sum / rate
                else -> {}
            }
        }
    }

    fun getAmountMap(): TreeMap<String, Account> {
        return accountTreeMap
    }

    fun getTotalAmount() = totalAmount
}