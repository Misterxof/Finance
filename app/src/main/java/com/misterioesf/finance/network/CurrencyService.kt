package com.misterioesf.finance.network

import com.misterioesf.finance.domain.model.Course
import retrofit2.http.GET

interface CurrencyService {

    @GET("/exrates/rates/USD?parammode=2")
    suspend fun getUSDCourse() : Course

    @GET("/exrates/rates/EUR?parammode=2")
    suspend fun getEURCourse() : Course
}