package com.example.registrationloginapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    private val prefsName = "gym_members_prefs"
    private val keyLoggedIn = "logged_in_member"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val txtDetails = findViewById<TextView>(R.id.txtUserDetails)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        val username = prefs.getString(keyLoggedIn, null)

        if (username.isNullOrBlank()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val record = prefs.getString("member_$username", null)
        if (record == null) {
            prefs.edit().remove(keyLoggedIn).apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val parts = record.split("|")
        val fullName = parts.getOrNull(0) ?: ""
        val gender = parts.getOrNull(1) ?: ""
        val year = parts.getOrNull(2) ?: ""
        val memberId = parts.getOrNull(3) ?: ""

        txtDetails.text =
            "Full name: $fullName\n" +
                    "Gender: $gender\n" +
                    "Year of birth: $year\n" +
                    "Member ID: $memberId\n\n" +
                    "${getString(R.string.status_active)}\n" +
                    getString(R.string.tip_text)

        btnLogout.setOnClickListener {
            prefs.edit().remove(keyLoggedIn).apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
