package com.example.papipel.Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.papipel.Models.Product

val DATABASE_NAME = "Papipel"
val TABLE_NAME = "Products"
val DATABASE_VERSION = 1
val COL_ID = "id"
val COL_PRODUCT_ID = "product_id"
val COL_NAME = "name"
val COL_CATEGORY = "category"
val COL_PRICE = "price"
val COL_QUANTITY = "quantity"
val COL_DESCRIPTION = "description"

class DatabaseProducts( var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_PRODUCT_ID + " VARCHAR(256)," +
                COL_NAME + " VARCHAR(256)," +
                COL_CATEGORY + " VARCHAR(256)," +
                COL_PRICE + " VARCHAR(256)," +
                COL_QUANTITY + " INTEGER," +
                COL_DESCRIPTION + " VARCHAR(256))"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    // Populate the products database updating or inserting new products
    fun populateDatabase(products: MutableList<Product>) : Boolean {
        val writeDB = this.writableDatabase
        var cv = ContentValues()
        var insert: Long = -1

        // For all the products in the list, update if the product is already in the list and insert
        // if not
        for (product in products) {
            cv.put(COL_PRODUCT_ID, product.id)
            cv.put(COL_NAME, product.name)
            cv.put(COL_CATEGORY, product.category)
            cv.put(COL_PRICE, product.price)
            cv.put(COL_QUANTITY, product.quantity)
            cv.put(COL_DESCRIPTION, product.description)

            // First try to update the row
            var update = writeDB.update(TABLE_NAME, cv, "$COL_PRODUCT_ID = " + product.id, null)

            // If the row does not exist, insert it
            if (update == 0) {
                insert = writeDB.insert(TABLE_NAME, null, cv)

                // Verify if occurred and error in insert. If occurred, stop the process and return
                if (insert == -1.toLong()) {
                    writeDB.close()
                    return false
                }
            }
        }

        writeDB.close()
        return true
    }

    // Get all the products in the list ordered by quantity of products
    fun getProductsOrderedByQuantity() : MutableList<Product> {
        var products : MutableList<Product> = ArrayList()

        // Query to get all the products
        val readDB = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME ORDER BY $COL_QUANTITY ASC;"
        val result = readDB.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var product = Product()
                product.id = result.getString(result.getColumnIndex(COL_ID))
                product.name = result.getString(result.getColumnIndex(COL_NAME))
                product.category = result.getString(result.getColumnIndex(COL_CATEGORY))
                product.price = result.getString(result.getColumnIndex(COL_PRICE)).toDouble()
                product.quantity = result.getInt(result.getColumnIndex(COL_QUANTITY))
                product.description = result.getString(result.getColumnIndex(COL_DESCRIPTION))
                products.add(product)
            } while (result.moveToNext())
        }

        readDB.close()
        result.close()
        return products
    }
}