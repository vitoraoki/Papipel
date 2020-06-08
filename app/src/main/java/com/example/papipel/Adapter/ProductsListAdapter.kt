package com.example.papipel.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.papipel.Models.Product
import com.example.papipel.R

class ProductsListAdapter (
    var mContext: Context,
    var resource: Int,
    var products: MutableList<Product>) : ArrayAdapter<Product>(mContext, resource, products) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(resource, null)

        // Get all the text views in the product row
        val productNameView = view.findViewById<TextView>(R.id.productName)
        val productCategoryView = view.findViewById<TextView>(R.id.productCategory)
        val productDescriptionView = view.findViewById<TextView>(R.id.productDescription)
        val productPriceView = view.findViewById<TextView>(R.id.productPrice)
        val productQuantityView = view.findViewById<TextView>(R.id.productQuantity)

        // Set the text views with the values from the product object
        var product = products.get(position)
        productNameView.text = product.name
        productCategoryView.text = product.category
        productDescriptionView.text = product.description
        productPriceView.text = "R\$" + product.price.toString().replace(".", ",")
        productQuantityView.text =
            product.quantity.toString() + if (product.quantity == 1) " unidade" else " unidades"


        return view
    }
}