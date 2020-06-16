package com.example.papipel.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.papipel.Adapter.OrderProductsAdapter
import com.example.papipel.Models.AppViewModel
import com.example.papipel.Models.Product
import com.example.papipel.R
import kotlinx.android.synthetic.main.fragment_order_products.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderProductsFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_order_products, container, false)
    }

    override fun onStart() {
        super.onStart()

        // Init the view model to store data between fragments
        appViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        inflateProductsOfOrder()
    }

    // Inflate the list of products in the order
    private fun inflateProductsOfOrder() {

        // Create the list with the products for the order
        var products = mutableListOf<Product>()
        val keys = appViewModel.orderProductsHash.keys()
        appViewModel.totalOrderPrice = 0.00
        keys.forEach { key ->
            val product = appViewModel.orderProductsHash.get(key) as Product
            products.add(product)
            appViewModel.totalOrderPrice += product.price
        }

        // Inflate the listview with the products
        val orderProductsListAdapter = OrderProductsAdapter(
            requireContext(),
            R.layout.order_products_list_row,
            products
        )
        lstv_order_products_list.adapter = orderProductsListAdapter

        // Handle with the item selected from the list
        lstv_order_products_list.setOnItemClickListener { parent, view, position, id ->
            val product = orderProductsListAdapter.getItem(position)
            removeProductOrder(product)
        }

        // Set the total price of the order
        var totalOrderPriceString = appViewModel.totalOrderPrice.toString().format("%.2f").replace(".", ",")
        total_order_price.text = "Total: R$ $totalOrderPriceString"
    }

    // Remove product for the order
    private fun removeProductOrder(product: Product?) {
        // Build the alert dialog
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
            .setTitle("Deseja remover o produto do pedido?")

        // With the confirmation, add the item in the list of items of the order
        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            appViewModel.orderProductsHash.remove(product?.name)
            inflateProductsOfOrder()
        }

        alertDialogBuilder.setNegativeButton("Cancelar") { dialog, which ->  }

        //Show the dialog
        alertDialogBuilder.show()
    }
}