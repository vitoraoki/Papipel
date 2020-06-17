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

class OrdersEListAdapter (val context: Context,
                          val orderList: MutableList<Order>,
                          val orderProductsList: HashMap<String, MutableList<Product>>): BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Any {
        return orderList[groupPosition]
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
            convertView = layoutInflater.inflate(R.layout.orders_elist_row, null)
        }

        // Get all the text views in the order row
        val orderIdView = convertView!!.findViewById<TextView>(R.id.txtv_order_elist_id)
        val orderValueView = convertView!!.findViewById<TextView>(R.id.txtv_order_elist_value)

        // Set the text views with the values from the order object
        val order = getGroup(groupPosition) as Order
        orderIdView.text = "Pedido ${order.id}"
        val orderValue = order.value.toString().format("%.2f").replace(".", ",")
        orderValueView.text = "R$ $orderValue"

        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return orderProductsList[orderList[groupPosition].id]!!.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return orderProductsList[orderList[groupPosition].id]!![childPosition]
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
            convertView = layoutInflater.inflate(R.layout.orders_products_elist_row, null)
        }

        // Get all the text views in the order product row
        val orderproductNameView = convertView!!.findViewById<TextView>(R.id.txtv_order_elist_product_name)

        // Set the text views with the values from the order object
        val product = getChild(groupPosition, childPosition) as Product
        val quantity = product.quantity.toString() + if (product.quantity == 1) " unidade" else " unidades"
        orderproductNameView.text = "- ${product.name}: $quantity"

        return convertView
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return orderList.size
    }
}