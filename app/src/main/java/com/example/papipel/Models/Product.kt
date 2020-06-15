package com.example.papipel.Models

class Product {
    var productId = ""
    var name = ""
    var category = ""
    var price = 0.0
    var quantity = 0
    var description = ""
    var active = 0

    constructor()
    constructor(productId: String, name: String, category: String, price: Double, quantity: Int,
                description: String, active: Int) {
        this.productId = productId
        this.name = name
        this.category = category
        this.price = price
        this.quantity = quantity
        this.description = description
        this.active = active
    }
}