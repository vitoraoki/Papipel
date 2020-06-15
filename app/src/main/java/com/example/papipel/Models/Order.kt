package com.example.papipel.Models

class Order {
    var id = ""
    var value = 0.0

    constructor()
    constructor(id: String, productPrice: Double, quantity: Int) {
        this.id = id
        this.value = orderValue(productPrice, quantity)
    }

    private fun orderValue(productPrice: Double, quantity: Int): Double {
        return productPrice * quantity
    }
}