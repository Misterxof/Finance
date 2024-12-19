package com.misterioesf.finance.domain.data

import com.misterioesf.finance.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    suspend fun getUSDCourse(): Flow<Course>

    suspend fun getEURCourse(): Flow<Course>
}