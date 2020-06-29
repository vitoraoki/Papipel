package com.example.papipel.Database

import android.content.ContentValues
import android.content.Context
import com.example.papipel.Models.Order
import com.example.papipel.Models.OrderProduct
import com.example.papipel.Models.Product

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

    // Get the products given an order
    fun getProductsGivenOrder(order: Order) : MutableList<Product> {

        // The list of products
        var products = mutableListOf<Product>()

        // Query to get all the products given an order
        val readDB = this.readableDatabase
        val query = "SELECT $COL_NAME, OP.$COL_QUANTITY, P.$COL_PRICE FROM $TABLE_NAME_ORDER_PRODUCTS as OP " +
                "INNER JOIN $TABLE_NAME_PRODUCTS as P ON OP.$COL_PRODUCT_ID = P.$COL_ID " +
                "WHERE OP.$COL_ORDER_ID = ${order.id};"
        val result = readDB.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var product = Product()
                product.name = result.getString(result.getColumnIndex(COL_NAME))
                product.quantity = result.getString(result.getColumnIndex(COL_QUANTITY)).toInt()
                product.price = result.getString(result.getColumnIndex(COL_PRICE)).toDouble()
                products.add(product)
            } while (result.moveToNext())
        }

        return products
    }
}