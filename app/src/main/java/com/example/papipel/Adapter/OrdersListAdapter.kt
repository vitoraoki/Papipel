package com.example.papipel.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.papipel.Models.Order
import com.example.papipel.R
import kotlin.math.round

class OrdersListAdapter (
    var mContext: Context,
    var resource: Int,
    var orders: MutableList<Order>) : ArrayAdapter<Order>(mContext, resource, orders) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val view = layoutInflater.inflate(resource, null)

        // Get all the text views in the order row
        val orderIdView = view.findViewById<TextView>(R.id.txtv_order_id)
        val orderValueView = view.findViewById<TextView>(R.id.txtv_order_value)

        // Set the text views with the values from the order object
        val order = orders.get(position)
        orderIdView.text = "Pedido ${order.id}"
        val orderValue = order.value.toString().format("%.2f").replace(".", ",")
        orderValueView.text = "R$ $orderValue"
        return view
    }
}