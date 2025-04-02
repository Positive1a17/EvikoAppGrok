package com.example.shopapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val modelUrl: String,
    val categoryId: String,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
) 