package com.budget.tracker.models

data class Expense(
    val id: Int,
    val amount: Float,
    val categoryName: String,
    val timeStamp: String,
    val currency: Currency
)