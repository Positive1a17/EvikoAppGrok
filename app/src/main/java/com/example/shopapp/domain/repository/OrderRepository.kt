package com.example.shopapp.domain.repository

import com.example.shopapp.domain.model.Order
import com.example.shopapp.domain.model.OrderStatus
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getUserOrders(userId: String): Flow<List<Order>>
    fun getOrderById(id: String): Flow<Order?>
    suspend fun createOrder(order: Order)
    suspend fun updateOrderStatus(id: String, status: OrderStatus)
    suspend fun cancelOrder(id: String)
} 