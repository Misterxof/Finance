package com.misterioesf.finance.repository

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.misterioesf.finance.CurrencyService
import com.misterioesf.finance.dao.FinanceDao
import com.misterioesf.finance.dao.entity.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CurrencyRepository(private val service: CurrencyService?) {

    suspend fun getCurrencyCourse(): Flow<Course> = flow {
            try {
                service?.let {
                    val response = it.getUSDCourse()
                    response?.let{ emit(it) }
                    Log.e("WWWWWWWWWWWWWW", response.toString())
                }
            } catch (e: Exception) {
                Log.e("WWWWWWWWWWWWWW", e.stackTraceToString())
            }
    }

    companion object {
        private var repository: CurrencyRepository? = null

        fun initialize(service: CurrencyService?) {
            if (repository == null) repository = CurrencyRepository(service)
        }

        fun getCurrencyRepository(): CurrencyRepository {
            return repository ?: throw java.lang.IllegalStateException("CurrencyRepository must be init")
        }
    }
}