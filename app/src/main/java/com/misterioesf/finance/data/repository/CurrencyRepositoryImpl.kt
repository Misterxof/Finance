package com.misterioesf.finance.data.repository

import android.util.Log
import com.misterioesf.finance.domain.data.CurrencyRepository
import com.misterioesf.finance.network.CurrencyService
import com.misterioesf.finance.domain.model.Course
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor(private val service: CurrencyService?): CurrencyRepository {

    override suspend fun getUSDCourse(): Flow<Course> = flow {
            try {
                service?.let {
                    val response = it.getUSDCourse()
                    response?.let{ emit(it) }
                }
            } catch (e: Exception) {
                Log.e("WWWWWWWWWWWWWW", e.stackTraceToString())
            }
    }

    override suspend fun getEURCourse(): Flow<Course> = flow {
        try {
            service?.let {
                val response = it.getEURCourse()
                response?.let{ emit(it) }
            }
        } catch (e: Exception) {
            Log.e("WWWWWWWWWWWWWW", e.stackTraceToString())
        }
    }
}