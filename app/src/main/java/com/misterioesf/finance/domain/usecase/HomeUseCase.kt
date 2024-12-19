package com.misterioesf.finance.domain.usecase

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.misterioesf.finance.ColorSetter
import com.misterioesf.finance.Utils
import com.misterioesf.finance.data.dao.entity.Account
import com.misterioesf.finance.domain.data.CurrencyRepository
import com.misterioesf.finance.domain.data.FinanceRepository
import com.misterioesf.finance.domain.model.Currencies
import com.misterioesf.finance.domain.model.StatefulData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import java.util.*
import javax.inject.Inject

class HomeUseCase @Inject constructor(
    private val financeRepository: FinanceRepository,
    private val currencyRepository: CurrencyRepository, @ApplicationContext val context: Context
): AccountUseCase(financeRepository) {

    private val _allCourses =
        MutableStateFlow<StatefulData<Pair<Float, Float>>>(StatefulData.LoadingState(1f to 1f))
    private val _totalAmounts =
        MutableStateFlow<StatefulData<HashMap<String, Any>>>(StatefulData.LoadingState(HashMap()))
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)

    private var rateUSD = 1f
    private var rateEU = 1f


    val allCourses = _allCourses.asStateFlow()
    val totalAmounts = _totalAmounts.asStateFlow()

    init {
        initData()
    }

    private fun initData() {
        calculateAccountAmount()
        getCourses()
    }

    private fun getCourses() {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            rateUSD = sharedPreferences?.getFloat("USD", 1f)!!
            rateEU = sharedPreferences?.getFloat("EUR", 1f)!!
            _allCourses.emit(StatefulData.SaveState(rateUSD to rateEU))

            if (Utils.isInternetOn(context)) {
                val usdCourse = withContext(Dispatchers.IO) { currencyRepository.getUSDCourse() }
                val euCourse = withContext(Dispatchers.IO) { currencyRepository.getEURCourse() }

                usdCourse.zip(euCourse) { usd, eu ->
                    Log.e("OMEUSECACSE", "esd $usd eur $eu")
                    sharedPreferences?.edit()?.putFloat("USD", usd.rate)?.apply()
                    sharedPreferences?.edit()?.putFloat("EUR", eu.rate)?.apply()
                    rateUSD = usd.rate
                    rateEU = eu.rate
                    _allCourses.emit(StatefulData.SuccessState(usd.rate to eu.rate))
                    _allAccounts.value.data?.let { calculateTotal(it) }
                }.collect {}
            }
        }
    }

    private fun calculateAccountAmount() {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            _allAccounts.collect {
                calculateTotal(it.data!!)
            }
        }
    }

    private fun calculateTotal(accountList: List<Account>) {
        CoroutineScope(Job() + Dispatchers.IO).launch {
            var eurTotalAmount = 0.0
            var usdTotalAmount = 0.0
            var bynTotalAmount = 0.0
            val accountTreeMap = TreeMap<String, Account>()

            ColorSetter.clear()

            accountList.forEach { account ->
                when (Currencies.valueOf(account.currency)) {
                    Currencies.USD -> {
                        eurTotalAmount += account.sum * rateUSD / rateEU
                        usdTotalAmount += account.sum
                        bynTotalAmount += account.sum * rateUSD
                    }
                    Currencies.BYN -> {
                        eurTotalAmount += account.sum / rateEU
                        usdTotalAmount += account.sum / rateUSD
                        bynTotalAmount += account.sum
                    }
                    Currencies.EU -> {
                        eurTotalAmount += account.sum
                        usdTotalAmount += account.sum * rateEU / rateUSD
                        bynTotalAmount += account.sum * rateEU
                    }
                    else -> {}
                }

                accountTreeMap[account.name] = account
                account.color = ColorSetter.getNextColor()
            }
            val result = HashMap<String, Any>()
            result["AccountTreeMap"] = accountTreeMap
            result["Totals"] = listOf(usdTotalAmount, eurTotalAmount, bynTotalAmount)

            _totalAmounts.emit(StatefulData.SuccessState(result))
        }
    }
}