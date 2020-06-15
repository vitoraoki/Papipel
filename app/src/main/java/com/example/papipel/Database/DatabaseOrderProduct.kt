package com.example.papipel.Database

import android.content.ContentValues
import android.content.Context
import com.example.papipel.Models.OrderProduct

class DatabaseOrderProduct(context: Context): DatabaseHelper(context) {

    // Insert the OrderProducts list in the database
    fun insertOrderProductsList(orderProductsList: MutableList<OrderProduct>): Boolean {
        val writeDB = this.writableDatabase
        var cv = ContentValues()

        for (orderProduct in orderProductsList) {
            cv.put(COL_ORDER_ID, orderProduct.orderId)
            cv.put(COL_PRODUCT_ID, orderProduct.productId)
            cv.put(COL_QUANTITY, orderProduct.quantity)

            val result = writeDB.insert(TABLE_NAME_ORDER_PRODUCTS, null, cv)

            if (result == -1.toLong()) {
                return false
            }
        }

        return true
    }
}