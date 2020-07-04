package com.example.papipel.Views

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import com.example.papipel.Adapter.OrdersEListAdapter
import com.example.papipel.Database.DatabaseOrderProduct
import com.example.papipel.Database.DatabaseOrders
import com.example.papipel.Models.Order
import com.example.papipel.Models.Product
import com.example.papipel.R
import kotlinx.android.synthetic.main.activity_orders_list.*
import kotlinx.android.synthetic.main.dialog_checkout.view.*
import java.io.File
import java.io.FileWriter
import java.lang.Exception
import java.lang.StringBuilder
import kotlin.math.exp

class OrdersListActivity : AppCompatActivity(), View.OnClickListener {

    private val databaseOrders = DatabaseOrders(this)
    private val databaseOrderProduct = DatabaseOrderProduct(this)

    private lateinit var ordersList: MutableList<Order>
    private var orderProductsList = HashMap<String, MutableList<Product>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_list)

        // Set the listeners with the buttons
        setListeners()

        // Show the list of orders
        inflateOrdersEList()

        // Show the back button in toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Set the buttons listener
    private fun setListeners() {
        btn_checkout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id

        if (id == R.id.btn_checkout) {

            // Inflate the dialog with the custom view
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_checkout, null)

            // Create the dialog builder
            val dialogBuilder = AlertDialog.Builder(this)
                .setView(dialogView)

            // Set the positive button
            dialogBuilder.setPositiveButton("Ok") { dialog, which ->

                // Get the values in the edittext
                val moneyCheckout = dialogView.edtxt_checkout_money
                val expensesCheckout = dialogView.edtxt_checkout_expenses

                // Verify if the fields are not empty
                if (!moneyCheckout.text.isEmpty() && !expensesCheckout.text.isEmpty()) {
                    checkout(moneyCheckout.text.toString().toDouble(),
                        expensesCheckout.text.toString().toDouble())
                } else {
                    Toast.makeText(this, "Complete os campos para continuar", Toast.LENGTH_SHORT).show()
                }
            }

            // Set the negative button
            dialogBuilder.setNegativeButton("Cancelar") { dialog, which ->  }

            // Show the dialog
            dialogBuilder.show()
        }
    }

    // Deal with the back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Inflate the extendable list of orders
    private fun inflateOrdersEList() {

        // Get the list of orders from database
        ordersList = databaseOrders.getAllOrders()

        // For each order get the products related and save in a hash
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

    // Export the csv file with the checkout
    private fun checkout(moneyCheckout: Double, expensesCheckout: Double) {

        // Get the name and the file type
        val nameFile = "caixa"
        val typeFile = "csv"
        var data = StringBuilder()

        // Calculate the total of the day
        var totalSold = 0.0
        ordersList.forEach { order ->
            totalSold += order.value
        }

        try {
            // Create the csv file with the data from the database
            data.append("Pedido,Produto,Quantidade,Preco,Total das Vendas,Dinheiro em Caixa,Gastos,Saldo Final")
            var firstLine = true
            orderProductsList.keys.forEach { key ->
                orderProductsList[key]?.forEach { product ->

                    if (firstLine) {
                        data.append("\n" + key + "," +
                                product.name + "," +
                                product.quantity.toString() + "," +
                                product.price.toString() + "," +
                                totalSold.toString() + "," +
                                moneyCheckout.toString() + "," +
                                expensesCheckout.toString() + "," +
                                (totalSold + moneyCheckout - expensesCheckout).toString())
                        firstLine = false
                    } else {
                        data.append("\n" + key + "," +
                                product.name + "," +
                                product.quantity.toString() + "," +
                                product.price.toString())
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao gerar o arquivo", Toast.LENGTH_SHORT).show()
        }

        try {
            // Save the csv file to be sent
            var fileOut = openFileOutput(nameFile + "." + typeFile, Context.MODE_PRIVATE)
            fileOut.write(data.toString().toByteArray())
            fileOut.close()

            // Get the file path to send it
            val fileLocation = File(filesDir, nameFile + "." + typeFile)
            val path = FileProvider.getUriForFile(this, "com.example.papipel.fileprovider", fileLocation)

            // Create the chooser to choose the app to send the file
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.type = "text/" + typeFile
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra(Intent.EXTRA_STREAM, path)
            startActivity(Intent.createChooser(intent, "Enviar"))

            // After share the csv file with the data, clean the database
            databaseOrders.cleanDatabase()
            databaseOrderProduct.cleanDatabase()
            inflateOrdersEList()

        } catch (e: Exception) {
            Toast.makeText(this, "Erro ao enviar o arquivo", Toast.LENGTH_SHORT).show()
        }
    }
}