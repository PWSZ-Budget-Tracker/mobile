package com.budget.tracker.api

import com.budget.tracker.dtos.loginPayload
import com.budget.tracker.models.Category
import com.budget.tracker.models.Expense
import com.budget.tracker.models.Goal
import com.budget.tracker.models.Income

data class LoginResponse(val payload: loginPayload)

data class RegisterResponse(val status: Boolean)

data class CommonResponse(val status: Boolean)

data class CategoryResponse(val payload: Array<Category>)

data class ExpensesResponse(val payload: Array<Expense>)

data class IncomesResponse(val payload: Array<Income>)

data class GoalsResponse(val payload: Array<Goal>)
