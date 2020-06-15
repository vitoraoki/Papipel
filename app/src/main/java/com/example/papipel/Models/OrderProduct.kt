package com.example.papipel.Models

class OrderProduct {
    var orderId = 0
    var productId = ""
    var quantity = 0

    constructor()
    constructor(orderId: Int, productId: String, quantity: Int) {
        this.orderId = orderId
        this.productId = productId
        this.quantity = quantity
    }
}