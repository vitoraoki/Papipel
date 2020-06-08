package com.example.papipel.Views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.papipel.Adapter.ProductsListAdapter
import com.example.papipel.Database.DatabaseProducts
import com.example.papipel.Models.Product
import com.example.papipel.R.layout
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val requestCode = 1
    private lateinit var path: String
    private val databaseProducts = DatabaseProducts(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        // Set listeners
        setButtonsListeners()

        inflateProductsList()
    }

    private fun setButtonsListeners() {
        openFileChooser.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id

        // Open file chooser to choose the csv file to update or create the list of products
        if (id == openFileChooser.id) {
            startFileChooser()
        }
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

    // Get the csv file and create a list of products with the data
    private fun csvToProductList(data: Intent?) : MutableList<Product>? {

        try {
            // Read the csv file
            var inputStream = contentResolver.openInputStream(data!!.data!!)
            var reader = BufferedReader(InputStreamReader(inputStream)).lineSequence().iterator()

            // Put the data from the csv file in a list of Products
            var products = mutableListOf<Product>()

            // Remove the first line that it is the header
            reader.next()

            // Get the products from the csv file and append in the list of products objects
            reader.forEach { line ->
                val product = line.split(",")
                products.add(
                    Product(
                        product.get(0), product.get(1), product.get(2), product.get(3).toDouble(),
                        product.get(4).toInt(), product.get(5)
                    )
                )
            }
            return products
        } catch (e: Exception) {
            return null
        }
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
                    message = "Banco de dados atualizado"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    inflateProductsList()
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

    // Inflate the list of products
    fun inflateProductsList() {

        // Get the list of products from database
        val products = databaseProducts.getProductsOrderedByQuantity()

        // Inflate the listview with the products
        val productsListAdapter = ProductsListAdapter(
            this,
            layout.products_list_row,
            products
        )
        productsList.adapter = productsListAdapter
    }
}