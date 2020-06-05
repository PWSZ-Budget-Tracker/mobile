package com.budget.tracker

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.budget.tracker.api.RetrofitClient
import com.budget.tracker.api.LoginResponse
import com.budget.tracker.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.support.v4.content.ContextCompat
import com.budget.tracker.requests.LoginRequest


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.theme_light))
        connectListeners()
    }

    override fun onStart() {
        super.onStart()

         if(SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }

    private fun connectListeners() {

        textViewRegister.setOnClickListener{
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY

            startActivity(intent)
        }

        buttonLogin.setOnClickListener {

            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if(!validateFields(email, password)) {
                return@setOnClickListener
            }

            RetrofitClient(applicationContext).instance.userLogin(LoginRequest(email, password))
                .enqueue(object : Callback<LoginResponse> {
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.code() == 200) {
                            SharedPrefManager.getInstance(applicationContext).saveToken(response.body()?.payload?.accessToken!!)

                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                            startActivity(intent)


                        } else {
                            Toast.makeText(applicationContext, getString(R.string.invalid_credentials), Toast.LENGTH_LONG).show()
                        }

                    }
                })
        }
    }

    private fun validateFields(email: String, password: String): Boolean {

        if (email.isEmpty()) {
            editTextEmail.error = getString(R.string.email_required)
            editTextEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            editTextPassword.error = getString(R.string.password_required)
            editTextPassword.requestFocus()
            return false
        }

        return true
    }
}
