package com.budget.tracker.requests

data class AddExpenseRequest(val categoryId: Int, val amount: Float, val currencyId: Int)