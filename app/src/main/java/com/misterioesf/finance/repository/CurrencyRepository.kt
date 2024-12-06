package com.misterioesf.finance.repository

import android.util.Log
import com.misterioesf.finance.network.CurrencyService
import com.misterioesf.finance.data.entity.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val service: CurrencyService?) {

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
}