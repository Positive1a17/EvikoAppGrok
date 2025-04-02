package com.example.shopapp.data.local.dao

import androidx.room.*
import com.example.shopapp.data.local.entity.OrderEntity
import com.example.shopapp.data.local.entity.OrderItemEntity
import com.example.shopapp.domain.model.Order
import com.example.shopapp.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Transaction
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun getUserOrders(userId: String): Flow<List<OrderEntity>>

    @Transaction
    @Query("SELECT * FROM orders WHERE id = :id")
    fun getOrderById(id: String): Flow<OrderEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Query("UPDATE orders SET status = :status, updatedAt = CURRENT_TIMESTAMP WHERE id = :id")
    suspend fun updateOrderStatus(id: String, status: OrderStatus)

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun deleteOrder(id: String)

    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    suspend fun deleteOrderItems(orderId: String)
}

@Dao
interface OrderItemDao {
    @Transaction
    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getOrderItems(orderId: String): Flow<List<OrderItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItem(item: OrderItemEntity)

    @Query("DELETE FROM order_items WHERE id = :id")
    suspend fun deleteOrderItem(id: String)
} 