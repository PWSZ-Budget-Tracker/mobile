package com.budget.tracker.api

import com.budget.tracker.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("/auth/login")
    fun userLogin(
        @Field("email") email:String,
        @Field("password") password: String
    ):Call<LoginResponse>

    @FormUrlEncoded
    @POST("/auth/register")
    fun userRegister(
        @Field("email") email:String,
        @Field("password") password: String
    ):Call<RegisterResponse>
}