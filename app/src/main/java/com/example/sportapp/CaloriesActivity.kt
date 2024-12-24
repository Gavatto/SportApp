package com.example.sportapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.sportapp.R

class CaloriesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calories)

        // Підключення до бази даних
        val dbHelper = DatabaseHelper(this)

        // Завантаження списку імен користувачів із бази даних
        val users = dbHelper.getUsernames()

        // Налаштування Spinner для вибору користувача
        val userSpinner = findViewById<Spinner>(R.id.userSpinner)
        val userAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, users)
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userSpinner.adapter = userAdapter

        // Поля для введення даних про продукт
        val productNameSpinner = findViewById<Spinner>(R.id.productSpinner)
        val productWeightEditText = findViewById<EditText>(R.id.productWeightEditText)
        val caloriesResultTextView = findViewById<TextView>(R.id.caloriesResultTextView)

        // Доступні продукти
        val products = mapOf(
            "Яблуко" to 52,
            "Банан" to 96,
            "Огірок" to 15,
            "Хліб" to 265,
            "Молоко" to 42
        )
        val productAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            products.keys.toList()
        )
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        productNameSpinner.adapter = productAdapter

        // Кнопка розрахунку калорій
        val calculateButton = findViewById<Button>(R.id.calculateCaloriesButton)
        calculateButton.setOnClickListener {
            val selectedProduct = productNameSpinner.selectedItem.toString()
            val productWeight = productWeightEditText.text.toString().trim()

            if (productWeight.isEmpty()) {
                Toast.makeText(this, "Будь ласка, введіть вагу продукту", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val weightInGrams = productWeight.toDouble()
                val caloriesPer100Grams = products[selectedProduct] ?: 0
                val totalCalories = (caloriesPer100Grams / 100.0) * weightInGrams

                caloriesResultTextView.text =
                    "У продукті $weightInGrams грам $selectedProduct міститься $totalCalories калорій"
            } catch (e: Exception) {
                Toast.makeText(this, "Невірний формат ваги", Toast.LENGTH_SHORT).show()
            }
        }

        // Кнопка переходу до вправ
        val goToExercisesButton = findViewById<Button>(R.id.goToExercisesButton)
        goToExercisesButton.setOnClickListener {
            val intent = Intent(this, ExercisesActivity::class.java)
            startActivity(intent)
        }

        // Кнопка відео дзвінка
        val goToVideoCallButton = findViewById<Button>(R.id.goToVideoCallButton)
        goToVideoCallButton.setOnClickListener {
            val selectedUser = userSpinner.selectedItem.toString()
            if (selectedUser.isEmpty()) {
                Toast.makeText(this, "Будь ласка, виберіть користувача", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val intent = Intent(this, VideoCallActivity::class.java)
            intent.putExtra("USERNAME", selectedUser)
            startActivity(intent)
        }
    }
}
