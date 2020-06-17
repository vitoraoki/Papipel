package com.example.papipel.Database

import android.content.ContentValues
import android.content.Context
import com.example.papipel.Models.Order
import com.example.papipel.Models.Product
import java.sql.SQLException

class DatabaseProducts(context: Context) : DatabaseHelper(context) {

    // Populate the products database updating or inserting new products
    fun populateDatabase(products: MutableList<Product>) : Boolean {
        val writeDB = this.writableDatabase
        var cv = ContentValues()
        var insert: Long = -1

        // For all the products in the list, update if the product is already in the list and insert
        // if not
        for (product in products) {
            cv.put(COL_PRODUCT_ID, product.productId)
            cv.put(COL_NAME, product.name)
            cv.put(COL_CATEGORY, product.category)
            cv.put(COL_PRICE, product.price)
            cv.put(COL_QUANTITY, product.quantity)
            cv.put(COL_DESCRIPTION, product.description)
            cv.put(COL_ACTIVE, product.active)

            // First try to update the row
            var update = writeDB.update(TABLE_NAME_PRODUCTS, cv, "$COL_PRODUCT_ID = " + product.productId, null)

            // If the row does not exist, insert it
            if (update == 0) {
                insert = writeDB.insert(TABLE_NAME_PRODUCTS, null, cv)

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
        var products = mutableListOf<Product>()

        // Query to get all the products
        val readDB = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_PRODUCTS WHERE $COL_ACTIVE = 1 ORDER BY $COL_QUANTITY ASC;"
        val result = readDB.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var product = Product()
                product.productId = result.getString(result.getColumnIndex(COL_PRODUCT_ID))
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

    // Get the categories of the products
    fun getCategories(): MutableList<String> {
        var categories = mutableListOf<String>()

        // Query to get all the categories
        val readDB = this.readableDatabase
        val query = "SELECT DISTINCT $COL_CATEGORY FROM $TABLE_NAME_PRODUCTS ORDER BY $COL_CATEGORY ASC;"
        val result = readDB.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var category = result.getString(result.getColumnIndex(COL_CATEGORY))
                categories.add(category)
            } while (result.moveToNext())
        }

        readDB.close()
        result.close()

        return categories
    }

    // Get the products given the category
    fun getProductByCategory(category: String): MutableList<Product> {
        var products = mutableListOf<Product>()

        // Query to get all the products by the category
        val readDB = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_PRODUCTS " +
                "WHERE $COL_ACTIVE = 1 AND $COL_CATEGORY = '$category' " +
                "AND $COL_QUANTITY > 0 ORDER BY $COL_NAME ASC;"
        val result = readDB.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var product = Product()
                product.id = result.getString(result.getColumnIndex(COL_ID))
                product.productId = result.getString(result.getColumnIndex(COL_PRODUCT_ID))
                product.name = result.getString(result.getColumnIndex(COL_NAME))
                product.price = result.getString(result.getColumnIndex(COL_PRICE)).toDouble()
                product.quantity = result.getString(result.getColumnIndex(COL_QUANTITY)).toInt()
                products.add(product)
            } while (result.moveToNext())
        }

        readDB.close()
        result.close()

        return products
    }

    // Update the quantity of a product
    fun updateProductQuantity(products: MutableList<Product>) : Boolean {

        val writeDB = this.writableDatabase

        for (product in products) {
            val sqlQuery = "UPDATE $TABLE_NAME_PRODUCTS " +
                    "SET $COL_QUANTITY = $COL_QUANTITY - ${product.quantity} " +
                    "WHERE $COL_ID = ${product.id}"
            try {
                writeDB.execSQL(sqlQuery)
            } catch (e: SQLException) {
                writeDB.close()
                return false
            }
        }

        writeDB.close()
        return true
    }
}