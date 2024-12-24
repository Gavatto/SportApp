package com.example.sportapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        dbHelper = DatabaseHelper(this)

        val Login = findViewById<TextView>(R.id.RegisterLogin)
        val Password = findViewById<TextView>(R.id.RegisterPassword)
        val Email = findViewById<TextView>(R.id.RegisterGmail)
        val ButtonRegister = findViewById<Button>(R.id.button)

        ButtonRegister.setOnClickListener {
            val loginText = Login.text.trim().toString()
            val passwordText = Password.text.trim().toString()
            val emailText = Email.text.trim().toString()

            if (loginText.isEmpty() || passwordText.isEmpty() || emailText.isEmpty()) {
                Toast.makeText(this, "Заповніть всі поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!dbHelper.isValidEmail(emailText)) {
                Toast.makeText(this, "Введіть правильну електронну пошту", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dbHelper.isLoginTaken(loginText)) {
                Toast.makeText(this, "Логін вже зайнятий", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dbHelper.registerUser(loginText, passwordText, emailText)) {
                Toast.makeText(this, "Користувач зареєстрований", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Помилка реєстрації", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
