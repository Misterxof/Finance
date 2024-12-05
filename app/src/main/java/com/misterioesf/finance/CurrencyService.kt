package com.misterioesf.finance

import com.misterioesf.finance.dao.entity.Course
import com.misterioesf.finance.dao.entity.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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