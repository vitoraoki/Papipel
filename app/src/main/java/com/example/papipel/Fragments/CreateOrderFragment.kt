package com.example.papipel.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.papipel.Adapter.ProductsByCategoryListAdapter
import com.example.papipel.Database.DatabaseProducts
import com.example.papipel.Models.AppViewModel
import com.example.papipel.Models.Product
import com.example.papipel.R
import kotlinx.android.synthetic.main.fragment_create_order.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateOrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateOrderFragment : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // Create the viewmodel to share data between the fragments
    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_order, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Load the spinner with the category
        loadSpinnerCategories()

        // Load the list of products for the first category on the list
        if (spn_order_spinner_categories.selectedItem != null) {
            inflateOrderProductsList()
        }

        // Init the view model to store data between fragments
        appViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
    }

    // Load all the categories to put in the spinner
    fun loadSpinnerCategories() {
        val databaseProducts = DatabaseProducts(requireContext())
        val categories = databaseProducts.getCategories()

        if (!categories.isEmpty()) {
            spn_order_spinner_categories.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                categories
            )
            spn_order_spinner_categories.onItemSelectedListener = this
        }
    }

    // Handle when nothing is selected in the spinner (will not be used)
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    // Handle with an item selected in the spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val id = parent?.id

        if (id == R.id.spn_order_spinner_categories) {
            inflateOrderProductsList()
        }
    }

    // Inflate the list of products with the first category
    fun inflateOrderProductsList() {

        // Get the list of products by category from database
        val databaseProducts = DatabaseProducts(requireContext())
        val products = databaseProducts.getProductByCategory(spn_order_spinner_categories.selectedItem.toString())

        // Inflate the listview with the products
        val productsByCategoryListAdapter = ProductsByCategoryListAdapter(
            requireContext(),
            R.layout.products_by_category_list_row,
            products
        )

        lstv_products_by_category_list.adapter = productsByCategoryListAdapter

        // Handle with the item selected from the list
        lstv_products_by_category_list.setOnItemClickListener { parent, view, position, id ->
            val product = productsByCategoryListAdapter.getItem(position)
            chooseQuantity(product)
        }
    }

    // Open a dialog to choose the number of items for that product
    private fun chooseQuantity(product: Product?) {
        // Create a number picker
        val numberPicker = NumberPicker(requireContext())
        numberPicker.maxValue = product!!.quantity
        numberPicker.minValue = 1

        // Build the alert dialog
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setView(numberPicker)
            .setTitle("Escolha a quantidade de items do pedido")

        // With the confirmation, add the item in the list of items of the order
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            try {
                var orderProduct = Product()
                orderProduct.name = product.name
                orderProduct.productId = product.productId
                orderProduct.quantity = numberPicker.value
                orderProduct.price = numberPicker.value * product.price
                appViewModel.orderProductsHash.put(product.name, orderProduct)
                Toast.makeText(requireContext(), "Produto adicionado ao pedido", Toast.LENGTH_SHORT)
                    .show()
            } catch (e: java.lang.Exception) {
                Toast.makeText(
                    requireContext(),
                    "Erro ao adicionar o produto ao pedido",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        alertDialogBuilder.setNegativeButton("Cancelar") { dialog, which -> }

        //Show the dialog
        alertDialogBuilder.show()
    }
}