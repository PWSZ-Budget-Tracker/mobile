package com.budget.tracker.requests

data class RegisterUserRequest(val email:String, val password:String, val passwordConfirmation:String)