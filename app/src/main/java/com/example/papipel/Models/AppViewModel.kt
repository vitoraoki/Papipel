package com.example.papipel.Models

import androidx.lifecycle.ViewModel
import org.json.JSONObject

class AppViewModel : ViewModel() {
    var orderProductsHash = JSONObject()
    var totalOrderPrice = 0.00
}