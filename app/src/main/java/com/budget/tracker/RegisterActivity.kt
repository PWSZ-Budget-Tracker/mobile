package com.budget.tracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.budget.tracker.api.RetrofitClient
import com.budget.tracker.api.RegisterResponse
import com.budget.tracker.storage.SharedPrefManager
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

            if(!validateFields(email, password)) {
                return@setOnClickListener
            }

            RetrofitClient(applicationContext).instance.userRegister(email, password)
                .enqueue(object : Callback<RegisterResponse> {
                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                        print(response.code())
                        if (response.code() == 200) {

                            SharedPrefManager.getInstance(applicationContext).saveToken(response.body()?.user?.token!!)

                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            startActivity(intent)


                        } else {
                            Toast.makeText(applicationContext, getString(R.string.invalid_register_data), Toast.LENGTH_LONG).show()
                        }

                    }
                })
        }
    }

    private fun validateFields(email: String, password: String): Boolean {

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

        return true
    }
}
