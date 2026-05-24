package com.example.eshorts.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ShortsDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "eshortyshop.db"
        const val DATABASE_VERSION = 1

        const val TABLE_PRODUCTS = "short_products"
        const val COL_ID = "id"
        const val COL_NAME = "name"
        const val COL_PRICE = "price"
        const val COL_DESCRIPTION = "description"
        const val COL_IMAGE_URL = "image_url"
        const val COL_IN_CART = "in_cart"
        const val COL_IS_FAVORITE = "is_favorite"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE $TABLE_PRODUCTS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NAME TEXT NOT NULL,
                $COL_PRICE REAL NOT NULL,
                $COL_DESCRIPTION TEXT NOT NULL,
                $COL_IMAGE_URL TEXT,
                $COL_IN_CART INTEGER NOT NULL DEFAULT 0,
                $COL_IS_FAVORITE INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent()

        db.execSQL(sql)

        db.execSQL("""
            INSERT INTO $TABLE_PRODUCTS ($COL_NAME, $COL_PRICE, $COL_DESCRIPTION, $COL_IMAGE_URL, $COL_IN_CART, $COL_IS_FAVORITE)
            VALUES ('Шорты Basic', 29.99, 'Базовые шорты на каждый день', '', 0, 0)
        """.trimIndent())

        db.execSQL("""
            INSERT INTO $TABLE_PRODUCTS ($COL_NAME, $COL_PRICE, $COL_DESCRIPTION, $COL_IMAGE_URL, $COL_IN_CART, $COL_IS_FAVORITE)
            VALUES ('Шорты Sport', 39.99, 'Спортивные шорты для активности', '', 0, 0)
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }
}