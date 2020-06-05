package com.budget.tracker.api

import com.budget.tracker.requests.*
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

    @POST("/api/Category/Add")
    fun addCategory(
        @Body addCategoryRequest : AddCategoryRequest
    ):Call<CommonResponse>

    @POST("/api/Expenses/Add")
    fun addNewExpense(
        @Body addExpenseRequest : AddExpenseRequest
    ):Call<CommonResponse>

    @DELETE("/api/Expenses/Delete/{expenseId}")
    fun removeExpense(
        @Path(value = "expenseId", encoded = true) expenseId: String?
    ):Call<CommonResponse>

    @POST("/api/Income/Add")
    fun addNewIncome(
        @Body addIncomesRequest : AddIncomeRequest
    ):Call<CommonResponse>

    @DELETE("/api/Income/Delete/{incomeId}")
    fun removeIncome(
        @Path(value = "incomeId", encoded = true) incomeId: String?
    ):Call<CommonResponse>

    @GET("/api/Category/GetAll")
    fun getCategories(
        @Query("type") categoryTypeId: Int?):Call<CategoryResponse>

    @GET("/api/Expenses/GetAll?date=2020-06-03")
    fun getExpenses():Call<ExpensesResponse>

    @GET("/api/Income/GetAll?date=2020-06-03")
    fun getIncomes():Call<IncomesResponse>

}