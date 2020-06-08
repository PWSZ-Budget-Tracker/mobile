package com.budget.tracker.models

data class Goal(
    val id: Int,
    val amount: Float,
    val goalAmount: Float,
    val name: String,
    val currency: Currency
)