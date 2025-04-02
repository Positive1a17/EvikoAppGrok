package com.example.shopapp.domain.model

import java.util.Date

data class Order(
    val id: String,
    val userId: String,
    val items: List<OrderItem>,
    val totalPrice: Double,
    val status: OrderStatus,
    val deliveryAddress: Address,
    val createdAt: Date,
    val updatedAt: Date
)

data class OrderItem(
    val product: Product,
    val quantity: Int,
    val price: Double
)

enum class OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
} 