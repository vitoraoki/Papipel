package com.example.papipel.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import com.example.papipel.Adapter.ProductsListAdapter
import com.example.papipel.Database.DatabaseProducts
import com.example.papipel.Models.AppViewModel
import com.example.papipel.R
import kotlinx.android.synthetic.main.dialog_checkout.view.*
import kotlinx.android.synthetic.main.dialog_sort_products.*
import kotlinx.android.synthetic.main.dialog_sort_products.view.*
import kotlinx.android.synthetic.main.fragment_all_products.*
import kotlinx.android.synthetic.main.fragment_create_order.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllProductsFragment : Fragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // Create the viewmodel to share data between the fragments
    private lateinit var appViewModel: AppViewModel

    // Position of the spinner to sort the list of products. It is used to set default value in the
    // spinner
    private var spinnerSortPosition = 0

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
        return inflater.inflate(R.layout.fragment_all_products, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Init the view model to store data between fragments
        appViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        // Show the list of products
        inflateProductsList(0)

        // Set the button listeners
        setButtonListeners()
    }

    private fun setButtonListeners() {
        btn_sort_products.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id

        // Open the dialog with the spinner to sort the list of products
        if (id == R.id.btn_sort_products) {
            openSortDialog()
        }
    }

    // Open the dialog to sort the products list
    private fun openSortDialog() {
        // Inflate the dialog with the custom view
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_sort_products, null)

        // Load the spinner with the data
        loadSpinnerSortCategories(dialogView)

        // Create the dialog builder
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        // Set the positive button
        dialogBuilder.setPositiveButton("Ok") { dialog, which ->
            inflateProductsList(dialogView.spn_sort_products.selectedItemPosition)
            spinnerSortPosition = dialogView.spn_sort_products.selectedItemPosition
        }

        // Set the negative button
        dialogBuilder.setNegativeButton("Cancelar") { dialog, which -> }

        // Show the dialog
        dialogBuilder.show()
    }

    // Load the spinner with the options to sort the products
    fun loadSpinnerSortCategories(dialogView: View) {
        val listSortOptions = listOf<String>(
            "Nome - Crescente",
            "Nome - Decrescente",
            "Quant. - Crescente",
            "Quant. - Decrescente"
        )

        dialogView.spn_sort_products.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            listSortOptions
        )
        dialogView.spn_sort_products.setSelection(spinnerSortPosition)
    }

    // Inflate the list of products
    fun inflateProductsList(sort: Int) {

        // Get the list of products from database
        val products = appViewModel.productsStockList

        // Sort the list of products
        if (sort == 0) {
            products.sortBy { it.name }
        } else if (sort == 1) {
            products.sortByDescending { it.name }
        } else if (sort == 2) {
            products.sortBy { it.quantity }
        } else if (sort == 3) {
            products.sortByDescending { it.quantity }
        }

        // Inflate the listview with the products
        val productsListAdapter = ProductsListAdapter(
            requireContext(),
            R.layout.products_list_row,
            products
        )
        lstv_products_list.adapter = productsListAdapter
    }
}