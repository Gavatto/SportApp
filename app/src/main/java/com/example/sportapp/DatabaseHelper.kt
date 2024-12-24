package com.example.sportapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Patterns

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "UsersDatabase.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, login TEXT UNIQUE, password TEXT, email TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun isLoginTaken(login: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM users WHERE login = ?", arrayOf(login))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun registerUser(login: String, password: String, email: String): Boolean {
        return try {
            val db = this.writableDatabase
            db.execSQL(
                "INSERT INTO users (login, password, email) VALUES (?, ?, ?)",
                arrayOf(login, password, email)
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    fun validateUser(login: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE login = ? AND password = ?",
            arrayOf(login, password)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
