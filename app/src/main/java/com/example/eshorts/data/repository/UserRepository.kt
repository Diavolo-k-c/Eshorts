package com.example.eshorts.data.repository

import android.content.ContentValues
import android.content.Context
import com.example.eshorts.data.db.ShortsDbHelper
import com.example.eshorts.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    context: Context
) {
    private val dbHelper = ShortsDbHelper(context)
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_CURRENT_USER_ID = "current_user_id"
    }

    suspend fun register(email: String, password: String): Result<User> = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        try {

            db.rawQuery(
                "SELECT id, email, password FROM users WHERE email = ? LIMIT 1",
                arrayOf(email)
            ).use { cursor ->
                if (cursor.moveToFirst()) {
                    return@withContext Result.failure(Exception("Пользователь с таким email уже существует"))
                }
            }

            val values = ContentValues().apply {
                put("email", email)
                put("password", password)
            }
            val newId = db.insert("users", null, values)
            if (newId == -1L) {
                return@withContext Result.failure(Exception("Не удалось создать пользователя"))
            }

            val user = User(id = newId, email = email, password = password)
            prefs.edit().putLong(KEY_CURRENT_USER_ID, newId).apply()

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentUser(): User? = withContext(Dispatchers.IO) {
        val id = prefs.getLong(KEY_CURRENT_USER_ID, -1L)
        if (id <= 0) return@withContext null

        val db = dbHelper.readableDatabase
        db.rawQuery(
            "SELECT id, email, password FROM users WHERE id = ? LIMIT 1",
            arrayOf(id.toString())
        ).use { cursor ->
            if (!cursor.moveToFirst()) return@withContext null
            val userId = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
            val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
            return@withContext User(id = userId, email = email, password = password)
        }
    }

    fun logout() {
        prefs.edit().remove(KEY_CURRENT_USER_ID).apply()
    }
}