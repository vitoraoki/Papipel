package com.example.papipel.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import com.example.papipel.Adapter.OrdersEListAdapter
import com.example.papipel.Database.DatabaseOrderProduct
import com.example.papipel.Database.DatabaseOrders
import com.example.papipel.Models.Product
import com.example.papipel.R
import kotlinx.android.synthetic.main.activity_orders_list.*

class OrdersListActivity : AppCompatActivity() {

    private val databaseOrders = DatabaseOrders(this)
    private val databaseOrderProduct = DatabaseOrderProduct(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_list)

        // Show the list of orders
        inflateOrdersEList()

        // Show the back button in toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Deal with the back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Inflate the extendable list of orders
    private fun inflateOrdersEList() {

        // Get the list of orders from database
        val ordersList = databaseOrders.getAllOrders()

        // For each order get the products related and save in a hash
        var orderProductsList = HashMap<String, MutableList<Product>>()
        for (order in ordersList) {

            // Get the product list for this order
            val productList = databaseOrderProduct.getProductsGivenOrder(order)
            orderProductsList.put(order.id, productList)
        }

        // Inflate the extendable list view
        val ordersEListAdapter = OrdersEListAdapter(this, ordersList, orderProductsList)
        elstv_orders_list.setAdapter(ordersEListAdapter)

        // Changing the indicator position to right
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        elstv_orders_list.setIndicatorBounds(width - 120, width)
    }
}