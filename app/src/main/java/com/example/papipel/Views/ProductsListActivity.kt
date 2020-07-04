package com.example.papipel.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.papipel.Adapter.ProductsListPageAdapter
import com.example.papipel.Database.DatabaseProducts
import com.example.papipel.Models.AppViewModel
import com.example.papipel.R
import kotlinx.android.synthetic.main.activity_products_list.*

class ProductsListActivity : AppCompatActivity() {

    private lateinit var fragmentAdpter: ProductsListPageAdapter
    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products_list)

        // Show the back button in toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Estoque")
        supportActionBar?.elevation = 0F

        // Create the fragment adapter
        fragmentAdpter = ProductsListPageAdapter(supportFragmentManager)
        vpager_products_list.adapter = fragmentAdpter

        // Set the tablayout with the viewpager
        lyt_tab_products_list.setupWithViewPager(vpager_products_list)
    }

    override fun onStart() {
        super.onStart()

        // Init the view model to store data between fragments
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        val databaseProducts = DatabaseProducts(this)
        appViewModel.productsStockList = databaseProducts.getProductsOrderedByQuantity()
    }

    // Deal with the back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}