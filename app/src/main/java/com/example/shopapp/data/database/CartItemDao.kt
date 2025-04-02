package com.example.shopapp.data.database

import androidx.room.*
import com.example.shopapp.data.models.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_items WHERE userId = :userId OR userId IS NULL")
    fun getCartItems(userId: String? = null): Flow<List<CartItem>>

    @Query("SELECT * FROM cart_items WHERE id = :id LIMIT 1")
    suspend fun getCartItemById(id: String): CartItem?

    @Query("SELECT * FROM cart_items WHERE productId = :productId AND (userId = :userId OR userId IS NULL) LIMIT 1")
    suspend fun getCartItemByProductId(productId: String, userId: String? = null): CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("DELETE FROM cart_items WHERE userId = :userId OR userId IS NULL")
    suspend fun clearCart(userId: String? = null)
    
    @Query("SELECT COUNT(*) FROM cart_items")
    suspend fun getCartItemsCount(): Int
} 