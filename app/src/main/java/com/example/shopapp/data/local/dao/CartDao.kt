package com.example.shopapp.data.local.dao

import androidx.room.*
import com.example.shopapp.data.local.entity.CartItemEntity
import com.example.shopapp.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Transaction
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Transaction
    @Query("SELECT * FROM cart_items WHERE id = :id")
    fun getCartItemById(id: String): Flow<CartItemEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateCartItemQuantity(id: String, quantity: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItem(id: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
} 