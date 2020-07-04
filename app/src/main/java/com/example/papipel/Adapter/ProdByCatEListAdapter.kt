package com.example.papipel.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.papipel.Models.Order
import com.example.papipel.Models.Product
import com.example.papipel.R

class ProdByCatEListAdapter(val context: Context,
                            val categoriesList: MutableList<String>,
                            val productsByCatList: HashMap<String, MutableList<Product>>): BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Any {
        return categoriesList[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        // Create the view for the order id and value (parent)
        var convertView = convertView
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.category_elist_row, null)
        }

        // Get all the text views in the order row
        val categoryNameView = convertView!!.findViewById<TextView>(R.id.txtv_category_elist_name)

        // Set the text views with the values from the order object
        categoryNameView.text = getGroup(groupPosition) as String

        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return productsByCatList[categoriesList[groupPosition]]!!.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return productsByCatList[categoriesList[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        // Create the view for the order product name
        var convertView = convertView
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.category_product_elist_row, null)
        }

        // Get all the text views in the order product row
        val productNameView = convertView!!.findViewById<TextView>(R.id.txtv_product_elist_name)
        val productDescriptionView = convertView!!.findViewById<TextView>(R.id.txtv_product_elist_description)
        val productPriceView = convertView!!.findViewById<TextView>(R.id.txtv_product_elist_price)
        val productQuantityView = convertView!!.findViewById<TextView>(R.id.txtv_product_elist_quantity)

        // Set the text views with the values from the product object
        val product = getChild(groupPosition, childPosition) as Product
        productNameView.text = product.name
        productDescriptionView.text = product.description
        productPriceView.text = "R\$" + product.price.toString().replace(".", ",")
        productQuantityView.text =
            product.quantity.toString() + if (product.quantity == 1) " unidade" else " unidades"

        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return categoriesList.size
    }
}