package com.example.shopapp.domain.model

data class CartItem(
    val id: String,
    val product: Product,
    val quantity: Int
) 