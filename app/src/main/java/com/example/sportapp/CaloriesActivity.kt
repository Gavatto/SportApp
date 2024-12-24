package com.example.sportapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sportapp.R


class CaloriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calories)

        // Поля для введення назви продукту та ваги
        val productNameEditText = findViewById<EditText>(R.id.productNameEditText)
        val productWeightEditText = findViewById<EditText>(R.id.productWeightEditText)

        // Поле для виводу результату
        val caloriesResultTextView = findViewById<TextView>(R.id.caloriesResultTextView)

        // Кнопка розрахунку калорій
        val calculateButton = findViewById<Button>(R.id.calculateCaloriesButton)
        calculateButton.setOnClickListener {
            val productName = productNameEditText.text.toString().trim()
            val productWeight = productWeightEditText.text.toString().trim()

            if (productName.isEmpty() || productWeight.isEmpty()) {
                Toast.makeText(this, "Будь ласка, заповніть усі поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val weightInGrams = productWeight.toDouble()
                val calorieData = mapOf(
                    "яблуко" to 52,
                    "банан" to 96,
                    "огірок" to 15,
                    "хліб" to 265,
                    "молоко" to 42,
                    "apple" to 52,
                    "banana" to 96,
                    "cucumber" to 15,
                    "bread" to 265,
                    "milk" to 42
                )

                val caloriesPer100Grams = calorieData[productName.lowercase()] ?: 0
                if (caloriesPer100Grams == 0) {
                    Toast.makeText(this, "Продукт не знайдено в базі даних", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val totalCalories = (caloriesPer100Grams / 100.0) * weightInGrams
                    caloriesResultTextView.text =
                        "У продукті $weightInGrams грам $productName міститься $totalCalories калорій"
                }

            } catch (e: Exception) {
                Toast.makeText(this, "Невірний формат ваги", Toast.LENGTH_SHORT).show()
            }
        }

        // Кнопка переходу до сторінки вправ
        val goToExercisesButton = findViewById<Button>(R.id.goToExercisesButton)
        goToExercisesButton.setOnClickListener {
            val intent = Intent(this, ExercisesActivity::class.java)
            startActivity(intent)
        }

        // Поле для введення імені
        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)

        // Кнопка переходу до відео дзвінка
        val goToVideoCallButton = findViewById<Button>(R.id.goToVideoCallButton)
        goToVideoCallButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            if (username.isEmpty()) {
                Toast.makeText(this, "Будь ласка, введіть ім'я користувача", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, VideoCallActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }
    }
}
