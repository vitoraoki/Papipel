package com.example.papipel.Fragments

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.papipel.Adapter.OrdersEListAdapter
import com.example.papipel.Adapter.ProdByCatEListAdapter
import com.example.papipel.Adapter.ProductsListAdapter
import com.example.papipel.Database.DatabaseProducts
import com.example.papipel.Models.AppViewModel
import com.example.papipel.Models.Order
import com.example.papipel.Models.Product
import com.example.papipel.R
import kotlinx.android.synthetic.main.activity_orders_list.*
import kotlinx.android.synthetic.main.fragment_products_by_category.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductsByCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductsByCategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // Create the viewmodel to share data between the fragments
    private lateinit var appViewModel: AppViewModel

    private lateinit var categoriesList: MutableList<String>
    private var productsByCatList = HashMap<String, MutableList<Product>>()

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
        return inflater.inflate(R.layout.fragment_products_by_category, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Init the view model to store data between fragments
        appViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        // Show the list of categories
        inflateOCategoriesEList()
    }

    // Inflate the extendable list of products separated by category
    private fun inflateOCategoriesEList() {

        // Get the list of products from database
        val products = appViewModel.productsStockList
        products.sortBy { it.name }

        // Get the list of categories from database
        val databaseProducts = DatabaseProducts(requireContext())
        categoriesList = databaseProducts.getCategories()

        // Put each product in the list of corresponded category
        for (product in products) {

            // If the category key was not created yet, create a new one
            if (!productsByCatList.containsKey(product.category)) {
                productsByCatList.put(product.category, mutableListOf(product))
            } else {
                productsByCatList[product.category]!!.add(product)
            }
        }

        // Inflate the extendable list view
        val categoriesEListAdapter = ProdByCatEListAdapter(requireContext(), categoriesList, productsByCatList)
        elstv_products_by_category.setAdapter(categoriesEListAdapter)

        // Changing the indicator position to right
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        elstv_products_by_category.setIndicatorBounds(width - 120, width)
    }

}