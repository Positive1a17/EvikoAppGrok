package com.example.shopapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val id: String,
    val productId: String,
    val name: String,
    val price: Double,
    val quantity: Int,
    val imageUrl: String,
    val userId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) 