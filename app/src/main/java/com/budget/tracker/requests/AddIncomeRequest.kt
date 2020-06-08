package com.budget.tracker.requests

data class AddIncomeRequest (val categoryId: Int, val amount:Double, val currencyId:Int)