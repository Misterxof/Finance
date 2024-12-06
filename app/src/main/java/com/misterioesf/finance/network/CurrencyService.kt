package com.misterioesf.finance.network

import com.misterioesf.finance.data.entity.Course
import retrofit2.http.GET

interface CurrencyService {

//    @GET("/users")
//    suspend fun getUsers(
//        @Query("per_page") per_page: Int,
//        @Query("page") page: Int
//    ): List<User>
//
//    @GET("/users/{username}")
//    suspend fun getUser(@Path("username") username: String) : User

    @GET("/exrates/rates/USD?parammode=2")
    suspend fun getUSDCourse() : Course
}