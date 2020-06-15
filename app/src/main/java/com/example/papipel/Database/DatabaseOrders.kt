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

        cv.put(COL_VALUE, order.value)

        return writeDB.insert(TABLE_NAME_ORDERS, null, cv)
    }

    // Get all the orders in the database
    fun getAllOrders(): MutableList<Order> {
        var orders = mutableListOf<Order>()

        // Query to get all the orders
        val readDB = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME_ORDERS;"
        val result = readDB.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var order = Order()
                order.id = result.getString(result.getColumnIndex(COL_ID))
                order.value = result.getString(result.getColumnIndex(COL_VALUE)).toDouble()
                orders.add(order)
            } while (result.moveToNext())
        }

        return orders
    }
}