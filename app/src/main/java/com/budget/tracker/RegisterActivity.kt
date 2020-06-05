package com.budget.tracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.budget.tracker.api.RetrofitClient
import com.budget.tracker.api.RegisterResponse
import com.budget.tracker.requests.RegisterUserRequest
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.theme_light))
        connectListeners()
    }

    private fun connectListeners() {

        buttonRegister.setOnClickListener {

            val email = registerEditTextEmail.text.toString().trim()
            val password = registerEditTextPassword.text.toString().trim()
            val passwordConfirmation = registerEditTextPasswordConfirmation.text.toString().trim()

            if(!validateFields(email, password, passwordConfirmation)) {
                return@setOnClickListener
            }

            RetrofitClient(applicationContext).instance.userRegister(
                RegisterUserRequest(
                    email,
                    password,
                    passwordConfirmation
                )
            )
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        if (response.code() == 200) {
//                            SharedPrefManager.getInstance(applicationContext).saveToken(response.body()?.token!!)

                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            Toast.makeText(applicationContext, getString(R.string.successful_register_process), Toast.LENGTH_LONG).show()

                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, getString(R.string.invalid_register_data), Toast.LENGTH_LONG).show()
                        }

                    }
                })
        }
    }

    private fun validateFields(email: String, password: String, passwordConfirmation: String): Boolean {

        if (email.isEmpty()) {
            registerEditTextEmail.error = getString(R.string.email_required)
            registerEditTextEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            registerEditTextPassword.error = getString(R.string.password_required)
            registerEditTextPassword.requestFocus()
            return false
        }

        if (passwordConfirmation.isEmpty()) {
            registerEditTextPassword.error = getString(R.string.password_required)
            registerEditTextPassword.requestFocus()
            return false
        }

        return true
    }
}
