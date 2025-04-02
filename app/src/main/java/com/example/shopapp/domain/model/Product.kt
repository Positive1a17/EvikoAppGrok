package com.example.shopapp.domain.model

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String? = null,
    val specifications: Map<String, String> = emptyMap()
) 