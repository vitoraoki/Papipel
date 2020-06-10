package com.example.papipel.Database

import android.content.ContentValues
import android.content.Context
import com.example.papipel.Models.Order
import com.example.papipel.Models.Product

class DatabaseOrders(context: Context) : DatabaseHelper(context) {

    // Insert a new order to the database
    fun insertOrder(order: Order): Long {
        val writeDB = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_PRODUCT_ID, order.productId)
        cv.put(COL_VALUE, order.value)
        cv.put(COL_DATE, order.date)

        return writeDB.insert(TABLE_NAME_ORDERS, null, cv)
    }

    // Get all the orders in the database
    fun getAllOrders(): MutableList<Order> {
        var orders = mutableListOf<Order>()

        // Query to get all the orders
        val readDB = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_ORDERS ORDER BY $COL_DATE ASC;"
        val result = readDB.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var order = Order()
                order.productId = result.getString(result.getColumnIndex(COL_PRODUCT_ID))
                order.value = result.getString(result.getColumnIndex(COL_VALUE)).toDouble()
                order.date = result.getString(result.getColumnIndex(COL_DATE))
                orders.add(order)
            } while (result.moveToNext())
        }

        return orders
    }
}