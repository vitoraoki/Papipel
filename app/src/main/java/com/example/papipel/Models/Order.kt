package com.example.papipel.Models

class Order {
    var id = ""
    var productId = ""
    var value = 0.0
    var date = ""

    constructor()
    constructor(id: String, productId: String, productPrice: Double, quantity: Int, date: String) {
        this.id = id
        this.productId = productId
        this.value = orderValue(productPrice, quantity)
        this.date = date
    }

    private fun orderValue(productPrice: Double, quantity: Int): Double {
        return productPrice * quantity
    }
}