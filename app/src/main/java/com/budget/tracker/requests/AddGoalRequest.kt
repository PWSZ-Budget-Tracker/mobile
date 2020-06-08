package com.budget.tracker.requests

data class AddGoalRequest(val name: String, val goalAmount: Float, val currencyId: Int)