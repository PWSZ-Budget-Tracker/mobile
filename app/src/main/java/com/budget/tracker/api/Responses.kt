package com.budget.tracker.api

import com.budget.tracker.dtos.loginPayload

data class LoginResponse(val payload: loginPayload)

data class RegisterResponse(val status: Boolean)
