package com.example.shopapp.domain.repository

import com.example.shopapp.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    fun getCartItemById(id: String): Flow<CartItem?>
    suspend fun addToCart(productId: String, quantity: Int)
    suspend fun updateCartItemQuantity(id: String, quantity: Int)
    suspend fun removeFromCart(id: String)
    suspend fun clearCart()
} 