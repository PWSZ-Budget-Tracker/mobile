package com.budget.tracker.api

import com.budget.tracker.requests.*
import retrofit2.Call
import retrofit2.http.*


interface Api {

    @POST("/api/Authentication/Login")
    fun userLogin(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @POST("/api/Authentication/Register")
    fun userRegister(
        @Body user: RegisterUserRequest
    ): Call<RegisterResponse>

    @POST("/api/Category/Add")
    fun addCategory(
        @Body addCategoryRequest: AddCategoryRequest
    ): Call<CommonResponse>

    @POST("/api/Expenses/Add")
    fun addNewExpense(
        @Body addExpenseRequest: AddExpenseRequest
    ): Call<CommonResponse>

    @POST("/api/Goal/Add")
    fun addNewGoal(
        @Body addGoalRequest: AddGoalRequest
    ): Call<CommonResponse>

    @PUT("/api/Expenses/Edit")
    fun editExpense(
        @Body editExpenseRequest: EditExpenseRequest
    ): Call<CommonResponse>

    @PUT("/api/Income/Edit")
    fun editIncome(
        @Body editIncomeRequest: EditIncomeRequest
    ): Call<CommonResponse>

    @PUT("/api/Goal/Edit")
    fun editGoal(
        @Body editGoalRequest: EditGoalRequest
    ): Call<CommonResponse>

    @PUT("/api/Goal/AddAmount")
    fun addToGoal(
        @Body addToGoalRequest: AddToGoalRequest
    ): Call<CommonResponse>

    @HTTP(method = "DELETE", path = "/api/Goal/DeleteAmount", hasBody = true)
    fun removeFromGoal(@Body deleteFromGoalRequest: RemoveFromGoalRequest): Call<CommonResponse>

    @HTTP(method = "DELETE", path = "/api/Expenses/Delete", hasBody = true)
    fun removeExpense(@Body deleteExpenseRequest: DeleteExpenseRequest): Call<CommonResponse>

    @HTTP(method = "DELETE", path = "/api/Goal/Delete", hasBody = true)
    fun removeGoal(@Body deleteGoalRequest: DeleteGoalRequest): Call<CommonResponse>

    @HTTP(method = "DELETE", path = "/api/Income/Delete", hasBody = true)
    fun removeIncome(@Body deleteIncomeRequest: DeleteIncomeRequest): Call<CommonResponse>

    @POST("/api/Income/Add")
    fun addNewIncome(
        @Body addIncomesRequest: AddIncomeRequest
    ): Call<CommonResponse>

    @GET("/api/Category/GetAll")
    fun getCategories(
        @Query("type") categoryTypeId: Int?
    ): Call<CategoryResponse>

    @GET("/api/Expenses/GetAll")
    fun getExpenses(
        @Query("date") date: String
    ): Call<ExpensesResponse>

    @GET("/api/Income/GetAll")
    fun getIncomes(
        @Query("date") date: String
    ): Call<IncomesResponse>

    @GET("/api/Goal/GetAll")
    fun getGoals(): Call<GoalsResponse>

}