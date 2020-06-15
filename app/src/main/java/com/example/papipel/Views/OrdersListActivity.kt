package com.example.papipel.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.papipel.Adapter.OrdersListAdapter
import com.example.papipel.Adapter.ProductsListAdapter
import com.example.papipel.Database.DatabaseOrders
import com.example.papipel.R
import kotlinx.android.synthetic.main.activity_orders_list.*
import kotlinx.android.synthetic.main.activity_products_list.*

class OrdersListActivity : AppCompatActivity() {

    private val databaseOrders = DatabaseOrders(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_list)

        // Show the list of orders
        inflateOrdersList()

        // Show the back button in toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Deal with the back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Inflate the list of orders
    fun inflateOrdersList() {

        // Get the list of orders from database
        val products = databaseOrders.getAllOrders()

        // Inflate the listview with the orders
        val ordersListAdapter = OrdersListAdapter(
            this,
            R.layout.orders_list_row,
            products
        )
        lstv_orders_list.adapter = ordersListAdapter
    }
}