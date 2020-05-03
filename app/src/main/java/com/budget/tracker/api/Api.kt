package com.budget.tracker.api

import com.budget.tracker.requests.LoginRequest
import com.budget.tracker.requests.RegisterUserRequest
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("/api/Authentication/Login")
    fun userLogin(
        @Body loginRequest : LoginRequest
    ):Call<LoginResponse>

    @POST("/api/Authentication/Register")
    fun userRegister(
        @Body user : RegisterUserRequest
    ):Call<RegisterResponse>
}