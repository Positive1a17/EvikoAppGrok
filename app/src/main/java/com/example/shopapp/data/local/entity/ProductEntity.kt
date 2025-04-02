package com.example.shopapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shopapp.domain.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val imageUrl: String?,
    val specifications: Map<String, String>
) {
    fun toProduct(): Product {
        return Product(
            id = id,
            name = name,
            description = description,
            price = price,
            category = category,
            imageUrl = imageUrl,
            specifications = specifications
        )
    }

    companion object {
        fun fromProduct(product: Product): ProductEntity {
            return ProductEntity(
                id = product.id,
                name = product.name,
                description = product.description,
                price = product.price,
                category = product.category,
                imageUrl = product.imageUrl,
                specifications = product.specifications
            )
        }
    }
} 