package com.example.papipel.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.papipel.Adapter.ProductsListAdapter
import com.example.papipel.Database.DatabaseProducts
import com.example.papipel.Models.AppViewModel
import com.example.papipel.R
import kotlinx.android.synthetic.main.fragment_all_products.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllProductsFragment : Fragment() {

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
        inflateProductsList()
    }

    // Inflate the list of products
    fun inflateProductsList() {

        // Get the list of products from database
        val products = appViewModel.productsStockList

        // Inflate the listview with the products
        val productsListAdapter = ProductsListAdapter(
            requireContext(),
            R.layout.products_list_row,
            products
        )
        lstv_products_list.adapter = productsListAdapter
    }
}