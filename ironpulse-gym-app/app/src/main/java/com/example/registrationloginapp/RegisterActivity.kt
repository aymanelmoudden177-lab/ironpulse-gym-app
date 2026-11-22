package com.example.registrationloginapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {

    private val prefsName = "gym_members_prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etFullName = findViewById<EditText>(R.id.etFullName)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val rbOther = findViewById<RadioButton>(R.id.rbOther)
        val etOtherGender = findViewById<EditText>(R.id.etOtherGender)
        val etYear = findViewById<EditText>(R.id.etYearOfBirth)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val etPass2 = findViewById<EditText>(R.id.etPassword2)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val txtMsg = findViewById<TextView>(R.id.txtRegisterMsg)

        // ✅ ADD THIS (Back button)
        val btnBackToLogin = findViewById<Button>(R.id.btnBackToLogin)
        btnBackToLogin.setOnClickListener {
            finish() // manual back to Login
        }

        rgGender.setOnCheckedChangeListener { _, checkedId ->
            etOtherGender.visibility = if (checkedId == rbOther.id) View.VISIBLE else View.GONE
            if (checkedId != rbOther.id) etOtherGender.text.clear()
        }

        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)

        btnRegister.setOnClickListener {
            txtMsg.setTextColor(0xFFB00020.toInt())
            txtMsg.text = ""

            val fullName = etFullName.text.toString().trim()
            val yearText = etYear.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPass.text.toString()
            val password2 = etPass2.text.toString()

            if (fullName.isBlank()) { etFullName.error = "Full name is required"; return@setOnClickListener }
            if (yearText.isBlank()) { etYear.error = "Year of birth is required"; return@setOnClickListener }
            if (username.isBlank()) { etUsername.error = "Member ID is required"; return@setOnClickListener }
            if (password.isBlank()) { etPass.error = "Password is required"; return@setOnClickListener }
            if (password2.isBlank()) { etPass2.error = "Re-enter your password"; return@setOnClickListener }

            val checkedId = rgGender.checkedRadioButtonId
            if (checkedId == -1) {
                txtMsg.text = getString(R.string.msg_select_gender)
                return@setOnClickListener
            }

            val gender = when (checkedId) {
                R.id.rbMale -> getString(R.string.gender_male)
                R.id.rbFemale -> getString(R.string.gender_female)
                R.id.rbNonBinary -> getString(R.string.gender_non_binary)
                R.id.rbOther -> {
                    val other = etOtherGender.text.toString().trim()
                    if (other.isBlank()) { etOtherGender.error = "Please type your gender"; return@setOnClickListener }
                    other
                }
                else -> "Unknown"
            }

            if (password != password2) {
                txtMsg.text = getString(R.string.msg_passwords_no_match)
                return@setOnClickListener
            }

            val year = yearText.toIntOrNull()
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            if (year == null || year < 1900 || year > currentYear) {
                etYear.error = "Enter a valid year (1900-$currentYear)"
                return@setOnClickListener
            }

            if (prefs.getString("member_$username", null) != null) {
                txtMsg.text = getString(R.string.msg_member_taken)
                return@setOnClickListener
            }

            val record = listOf(fullName, gender, year.toString(), username, password).joinToString("|")

            prefs.edit()
                .putString("member_$username", record)
                .apply()

            txtMsg.setTextColor(0xFF166534.toInt())
            txtMsg.text = getString(R.string.msg_register_success)

            // ✅ automatic back after success
            finish()
        }
    }
}
