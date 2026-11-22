package com.example.registrationloginapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private val prefsName = "gym_members_prefs"
    private val keyLoggedIn = "logged_in_member"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUser = findViewById<EditText>(R.id.etLoginUsername)
        val etPass = findViewById<EditText>(R.id.etLoginPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val txtError = findViewById<TextView>(R.id.txtLoginError)
        val txtGoRegister = findViewById<TextView>(R.id.txtGoRegister)
        txtGoRegister.paint.isUnderlineText = true



        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)

        val logged = prefs.getString(keyLoggedIn, null)
        if (!logged.isNullOrBlank()) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
            return
        }

        txtGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            txtError.text = ""

            val username = etUser.text.toString().trim()
            val password = etPass.text.toString()

            if (username.isBlank()) { etUser.error = "Enter your Member ID"; return@setOnClickListener }
            if (password.isBlank()) { etPass.error = "Enter your password"; return@setOnClickListener }

            val record = prefs.getString("member_$username", null)
            if (record == null) {
                txtError.text = getString(R.string.err_member_not_found)
                return@setOnClickListener
            }

            val savedPassword = record.split("|").getOrNull(4) ?: ""
            if (password != savedPassword) {
                txtError.text = getString(R.string.err_incorrect_password)
                return@setOnClickListener
            }

            prefs.edit().putString(keyLoggedIn, username).apply()
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }
    }
}

