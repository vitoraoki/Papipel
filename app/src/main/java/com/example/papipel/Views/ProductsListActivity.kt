package com.example.papipel.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.papipel.Adapter.ProductsListAdapter
import com.example.papipel.Database.DatabaseProducts
import com.example.papipel.R
import kotlinx.android.synthetic.main.activity_products_list.*

class ProductsListActivity : AppCompatActivity() {

    private val databaseProducts = DatabaseProducts(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)

        // Show the list of products
        inflateProductsList()

        // Show the back button in toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Deal with the back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Inflate the list of products
    fun inflateProductsList() {

        // Get the list of products from database
        val products = databaseProducts.getProductsOrderedByQuantity()

        // Inflate the listview with the products
        val productsListAdapter = ProductsListAdapter(
            this,
            R.layout.products_list_row,
            products
        )
        lstv_products_list.adapter = productsListAdapter
    }
}