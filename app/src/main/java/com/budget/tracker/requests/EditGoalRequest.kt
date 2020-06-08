package com.budget.tracker.requests

data class EditGoalRequest(val goalId: Int, val name: String, val goalAmount: Float)