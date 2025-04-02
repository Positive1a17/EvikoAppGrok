package com.example.shopapp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.shopapp.domain.model.CartItem

@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CartItemEntity(
    @PrimaryKey
    val id: String,
    val productId: String,
    val quantity: Int
) {
    fun toCartItem(product: ProductEntity? = null): CartItem {
        return CartItem(
            id = id,
            product = product?.toProduct() ?: Product(
                id = productId,
                name = "",
                description = "",
                price = 0.0,
                category = "",
                imageUrl = null,
                specifications = emptyMap()
            ),
            quantity = quantity
        )
    }

    companion object {
        fun fromCartItem(cartItem: CartItem): CartItemEntity {
            return CartItemEntity(
                id = cartItem.id,
                productId = cartItem.product.id,
                quantity = cartItem.quantity
            )
        }
    }
} 