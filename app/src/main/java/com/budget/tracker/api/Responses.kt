package com.budget.tracker.api

import com.budget.tracker.dtos.loginPayload
import com.budget.tracker.models.Category
import com.budget.tracker.models.Expense

data class LoginResponse(val payload: loginPayload)

data class RegisterResponse(val status: Boolean)

data class CommonResponse(val status: Boolean)

data class CategoryResponse(val payload: Array<Category>)

data class ExpensesResponse(val payload: Array<Expense>)
