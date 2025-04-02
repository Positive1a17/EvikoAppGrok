package com.example.shopapp.data.repository

import com.example.shopapp.data.local.dao.CartDao
import com.example.shopapp.data.local.dao.ProductDao
import com.example.shopapp.data.local.entity.CartItemEntity
import com.example.shopapp.domain.model.CartItem
import com.example.shopapp.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val productDao: ProductDao
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getCartItems().map { entities ->
            entities.map { entity ->
                entity.toCartItem()
            }
        }
    }

    override fun getCartItemById(id: String): Flow<CartItem?> {
        return cartDao.getCartItemById(id).map { entity ->
            entity?.toCartItem()
        }
    }

    override suspend fun addToCart(productId: String, quantity: Int) {
        val existingItem = cartDao.getCartItemById(productId).map { it?.toCartItem() }.value
        if (existingItem != null) {
            cartDao.updateCartItemQuantity(existingItem.id, existingItem.quantity + quantity)
        } else {
            val newItem = CartItemEntity(
                id = UUID.randomUUID().toString(),
                productId = productId,
                quantity = quantity
            )
            cartDao.insertCartItem(newItem)
        }
    }

    override suspend fun updateCartItemQuantity(id: String, quantity: Int) {
        cartDao.updateCartItemQuantity(id, quantity)
    }

    override suspend fun removeFromCart(id: String) {
        cartDao.deleteCartItem(id)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
} 