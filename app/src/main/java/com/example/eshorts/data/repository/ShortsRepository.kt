package com.example.eshorts.data.repository

import android.content.ContentValues
import android.content.Context
import com.example.eshorts.data.db.ShortsDbHelper
import com.example.eshorts.data.model.ShortProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShortsRepository(context: Context) {

    private val dbHelper = ShortsDbHelper(context.applicationContext)

    suspend fun getAllProducts(): List<ShortProduct> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            ShortsDbHelper.TABLE_PRODUCTS,
            null,
            null,
            null,
            null,
            null,
            "${ShortsDbHelper.COL_ID} DESC"
        )

        val list = mutableListOf<ShortProduct>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(cursorToProduct(it))
            }
        }
        list
    }

    suspend fun getProductById(id: Int): ShortProduct? = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            ShortsDbHelper.TABLE_PRODUCTS,
            null,
            "${ShortsDbHelper.COL_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        cursor.use {
            return@withContext if (it.moveToFirst()) cursorToProduct(it) else null
        }
    }

    suspend fun getCartProducts(): List<ShortProduct> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            ShortsDbHelper.TABLE_PRODUCTS,
            null,
            "${ShortsDbHelper.COL_IN_CART} = ?",
            arrayOf("1"),
            null,
            null,
            "${ShortsDbHelper.COL_ID} DESC"
        )

        val list = mutableListOf<ShortProduct>()
        cursor.use {
            while (it.moveToNext()) {
                list.add(cursorToProduct(it))
            }
        }
        list
    }

    suspend fun updateProduct(product: ShortProduct) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ShortsDbHelper.COL_NAME, product.name)
            put(ShortsDbHelper.COL_PRICE, product.price)
            put(ShortsDbHelper.COL_DESCRIPTION, product.description)
            // строка со всеми картинками
            put(ShortsDbHelper.COL_IMAGE_URL, product.imageUrlsRaw)
            put(ShortsDbHelper.COL_IN_CART, if (product.inCart) 1 else 0)
            put(ShortsDbHelper.COL_IS_FAVORITE, if (product.isFavorite) 1 else 0)
            put(ShortsDbHelper.COL_QUANTITY, product.quantity)
        }

        db.update(
            ShortsDbHelper.TABLE_PRODUCTS,
            values,
            "${ShortsDbHelper.COL_ID} = ?",
            arrayOf(product.id.toString())
        )
    }

    private fun cursorToProduct(cursor: android.database.Cursor): ShortProduct {
        return ShortProduct(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(ShortsDbHelper.COL_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(ShortsDbHelper.COL_NAME)),
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(ShortsDbHelper.COL_PRICE)),
            description = cursor.getString(cursor.getColumnIndexOrThrow(ShortsDbHelper.COL_DESCRIPTION)),
            imageUrlsRaw = cursor.getString(cursor.getColumnIndexOrThrow(ShortsDbHelper.COL_IMAGE_URL)) ?: "",
            inCart = cursor.getInt(cursor.getColumnIndexOrThrow(ShortsDbHelper.COL_IN_CART)) == 1,
            isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow(ShortsDbHelper.COL_IS_FAVORITE)) == 1,
            quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ShortsDbHelper.COL_QUANTITY))
        )
    }
}