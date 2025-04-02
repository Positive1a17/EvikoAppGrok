package com.example.shopapp.data.repositories

import com.example.shopapp.data.database.CartItemDao
import com.example.shopapp.data.models.CartItem
import com.example.shopapp.utils.SecurityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(productId: String, name: String, price: Double, quantity: Int, imageUrl: String)
    suspend fun increaseQuantity(cartItemId: String)
    suspend fun decreaseQuantity(cartItemId: String)
    suspend fun removeFromCart(cartItemId: String)
    suspend fun clearCart()
}

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartItemDao: CartItemDao,
    private val securityUtils: SecurityUtils,
    private val authRepository: AuthRepository
) : CartRepository {
    
    override fun getCartItems(): Flow<List<CartItem>> = 
        cartItemDao.getCartItems()
    
    override suspend fun addToCart(
        productId: String, 
        name: String, 
        price: Double, 
        quantity: Int, 
        imageUrl: String
    ) = withContext(Dispatchers.IO) {
        val userId = authRepository.getCurrentUser()?.id
        
        val existingItem = cartItemDao.getCartItemByProductId(productId)
        if (existingItem != null) {
            cartItemDao.updateCartItem(existingItem.copy(quantity = existingItem.quantity + quantity))
        } else {
            val cartItem = CartItem(
                id = securityUtils.generateUUID(),
                productId = productId,
                name = name,
                price = price,
                quantity = quantity,
                imageUrl = imageUrl,
                userId = userId
            )
            cartItemDao.insertCartItem(cartItem)
        }
    }
    
    override suspend fun increaseQuantity(cartItemId: String) = withContext(Dispatchers.IO) {
        val cartItem = cartItemDao.getCartItemById(cartItemId)
        cartItem?.let {
            cartItemDao.updateCartItem(it.copy(quantity = it.quantity + 1))
        }
    }
    
    override suspend fun decreaseQuantity(cartItemId: String) = withContext(Dispatchers.IO) {
        val cartItem = cartItemDao.getCartItemById(cartItemId)
        cartItem?.let {
            if (it.quantity > 1) {
                cartItemDao.updateCartItem(it.copy(quantity = it.quantity - 1))
            } else {
                cartItemDao.deleteCartItem(it)
            }
        }
    }
    
    override suspend fun removeFromCart(cartItemId: String) = withContext(Dispatchers.IO) {
        val cartItem = cartItemDao.getCartItemById(cartItemId)
        cartItem?.let {
            cartItemDao.deleteCartItem(it)
        }
    }
    
    override suspend fun clearCart() = withContext(Dispatchers.IO) {
        val userId = authRepository.getCurrentUser()?.id
        cartItemDao.clearCart(userId)
    }
} 