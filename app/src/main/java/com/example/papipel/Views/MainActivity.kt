package com.example.papipel.Views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.papipel.Adapter.PageAdapter
import com.example.papipel.Database.DatabaseOrderProduct
import com.example.papipel.Database.DatabaseOrders
import com.example.papipel.Database.DatabaseProducts
import com.example.papipel.Fragments.CreateOrderFragment
import com.example.papipel.Fragments.OrderProductsFragment
import com.example.papipel.Models.AppViewModel
import com.example.papipel.Models.Order
import com.example.papipel.Models.OrderProduct
import com.example.papipel.Models.Product
import com.example.papipel.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {

    private val requestCode = 1
    private val databaseProducts = DatabaseProducts(this)
    private val databaseOrders = DatabaseOrders(this)
    private val databaseOrderProduct = DatabaseOrderProduct(this)
    private lateinit var appViewModel: AppViewModel
    private lateinit var fragmentAdpter: PageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set listeners
        setButtonsListeners()

        // Create the toolbar with the navigation drawer to have a menu
        setSupportActionBar(toolbar as Toolbar?)
        val togle = ActionBarDrawerToggle(this, drawer_layout,
            toolbar as Toolbar?, R.string.nav_open_drawer, R.string.nav_close_drawer)
        drawer_layout.addDrawerListener(togle)
        togle.syncState()

        // Make the menu clickable and deal with the click actions
        nav_view.bringToFront()
        nav_view.setNavigationItemSelectedListener(this)

        // Create the fragment adapter
        fragmentAdpter = PageAdapter(supportFragmentManager)
        view_pager.adapter = fragmentAdpter

        // Set the tablayout with the viewpager
        lyt_tab.setupWithViewPager(view_pager)
    }

    override fun onStart() {
        super.onStart()

        // Init the view model to store data between fragments
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
    }

    override fun onBackPressed() {

        // Close just the navigation drawer when back button is pressed
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setButtonsListeners() {
        btn_close_order.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id

        // Deal with the click on the button of close an order
        if (id == R.id.btn_close_order) {

            // Open a dialog with the list of items of the order and the confirmation or not to
            // close the order
            closeOrder()
        }
    }

    // Open a dialog to confirm the close of the order and save the order in the database
    private fun closeOrder() {

        // Build the alert dialog
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setTitle("Deseja concluir o pedido?")

        // Set the positive button
        alertDialogBuilder.setPositiveButton("Sim") { dialog, which ->

            var resultInsertOrderProducts = false
            var resultUpdateQuantityProducts = false

            // If the order is empty, do not do anything
            if (appViewModel.totalOrderPrice > 0.0) {
                // Insert the order in the database
                var order = Order()
                order.value = appViewModel.totalOrderPrice
                val orderID = databaseOrders.insertOrder(order)

                // Insert the products of the order in the table OrderProducts
                if (orderID != -1.toLong()) {
                    var orderProductsList = mutableListOf<OrderProduct>()
                    appViewModel.orderProductsHash.keys.forEach { key ->
                        val product = appViewModel.orderProductsHash.get(key) as Product
                        val orderProduct = OrderProduct(orderID.toInt(), product.id, product.quantity)
                        orderProductsList.add(orderProduct)
                    }
                    resultInsertOrderProducts = databaseOrderProduct.insertOrderProductsList(orderProductsList)

                    // Update the quantity of products in the database
                    if (resultInsertOrderProducts) {
                        var products = mutableListOf<Product>()
                        appViewModel.orderProductsHash.keys.forEach { key ->
                            products.add(appViewModel.orderProductsHash.get(key) as Product)
                        }
                        resultUpdateQuantityProducts = databaseProducts.updateProductQuantity(products)
                    }
                }

                if ((orderID != -1.toLong()) && resultInsertOrderProducts && resultUpdateQuantityProducts) {
                    // Clean the totalPrice of the order and the hash with the products of the order
                    appViewModel.totalOrderPrice = 0.00
                    appViewModel.orderProductsHash = HashMap<String, Product>()

                    // Reload all the data to create an order
                    reloadDataFragments()

                    Toast.makeText(this, "Pedido concluído", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Erro ao concluir o pedido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Pedido vazio", Toast.LENGTH_SHORT).show()
            }

        }

        // Set the negative button
        alertDialogBuilder.setNegativeButton("Não") { dialog, which -> }

        //Show the dialog
        alertDialogBuilder.show()
    }

    // Deal with the click on the navigation drawer buttons
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.update_products_list) {
            startFileChooser()
        } else if (item.itemId == R.id.activity_products_list) {
            val intent = Intent(this, ProductsListActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.activity_orders_list) {
            val intent = Intent(this, OrdersListActivity::class.java)
            startActivity(intent)
        }

        // Close the drawer after the button is clicked and unselect all the items
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    // Start the Intent with the file chooser
    private fun startFileChooser() {
        val intent = Intent()
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("*/*")
        val mimeTypes = arrayOf("text/csv", "text/comma-separated-values")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.putExtra("android.content.extra.SHOW_ADVANCED", true)
        startActivityForResult(intent, requestCode)

        //        intent.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE)
//        intent.putExtra("android.content.extra.SHOW_ADVANCED", true)
//        startActivityForResult(intent, 2)
    }

    // Override the function to deal with the file chosen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (this.requestCode == requestCode && resultCode == Activity.RESULT_OK && data != null) {

            // Transform csv file to product objects list
            val products = csvToProductList(data)

            // If no errors occurred inflate the list of products stored
            if (products != null) {

                // Populate the database of products
                val result = databaseProducts.populateDatabase(products)
                var message = ""
                if (result) {

                    // Reload all the data to create an order
                    reloadDataFragments()

                    message = "Banco de dados atualizado"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                } else {
                    message = "Erro ao atualizar o banco de dados. Tente novamente"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            } else {
                val errorMessage = "Erro ao importar os arquivos. Tente novamente"
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Get the csv file and create a list of products with the data
    private fun csvToProductList(data: Intent?) : MutableList<Product>? {

        try {
            // Read the csv file
            var inputStream = contentResolver.openInputStream(data!!.data!!)
            var reader = BufferedReader(InputStreamReader(inputStream)).lineSequence().iterator()

            // Put the data from the csv file in a list of Products
            var products = mutableListOf<Product>()

            // Remove the first line that it is the header_navigation_drawer
            reader.next()

            // Get the products from the csv file and append in the list of products objects
            reader.forEach { line ->
                val product = line.split(",")
                products.add(
                    Product(
                        product.get(0), product.get(1), product.get(2), product.get(3).toDouble(),
                        product.get(4).toInt(), product.get(5), product.get(6).toInt()
                    )
                )
            }
            return products
        } catch (e: Exception) {
            return null
        }
    }

    private fun reloadDataFragments() {
        // Reload all the data to create an order
        val createOrderFragment = fragmentAdpter.getItem(0) as CreateOrderFragment
        val orderProductsFragment = fragmentAdpter.getItem(1) as OrderProductsFragment

        createOrderFragment.loadSpinnerCategories()
        createOrderFragment.inflateOrderProductsList()
        orderProductsFragment.onResume()
    }
}