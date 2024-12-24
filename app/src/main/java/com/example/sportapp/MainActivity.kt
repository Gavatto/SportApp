package com.example.sportapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val Login = findViewById<TextView>(R.id.Login_Lable)
        val Password = findViewById<TextView>(R.id.Password_Lable)
        val Button: Button = findViewById(R.id.Button)
        val LinkReg: TextView = findViewById(R.id.RegisterLink)

        Button.setOnClickListener {
            val loginText = Login.text.trim().toString()
            val passwordText = Password.text.trim().toString()

            if (loginText.isEmpty()) {
                Toast.makeText(this, "Введіть будь ласка свій логін", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (passwordText.isEmpty()) {
                Toast.makeText(this, "Введіть будь ласка свій пароль", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dbHelper.validateUser(loginText, passwordText)) {
                Toast.makeText(this, "Успішно", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CaloriesActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Неправильний логін або пароль", Toast.LENGTH_SHORT).show()
            }
        }

        LinkReg.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}