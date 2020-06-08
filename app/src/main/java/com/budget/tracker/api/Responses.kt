package com.budget.tracker.api

import com.budget.tracker.models.*

data class LoginResponse(val payload: LoginPayload)

data class RegisterResponse(val status: Boolean)

data class CommonResponse(val status: Boolean)

data class CategoryResponse(val payload: Array<Category>)

data class ExpensesResponse(val payload: Array<Expense>)

data class IncomesResponse(val payload: Array<Income>)

data class GoalsResponse(val payload: Array<Goal>)
