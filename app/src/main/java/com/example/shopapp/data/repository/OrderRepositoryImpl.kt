package com.example.shopapp.data.repository

import com.example.shopapp.data.local.dao.OrderDao
import com.example.shopapp.data.local.dao.OrderItemDao
import com.example.shopapp.data.local.entity.OrderEntity
import com.example.shopapp.data.local.entity.OrderItemEntity
import com.example.shopapp.domain.model.Order
import com.example.shopapp.domain.model.OrderStatus
import com.example.shopapp.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
) : OrderRepository {

    override fun getUserOrders(userId: String): Flow<List<Order>> {
        return orderDao.getUserOrders(userId).map { orders ->
            orders.map { order ->
                order.toOrder()
            }
        }
    }

    override fun getOrderById(id: String): Flow<Order?> {
        return orderDao.getOrderById(id).map { order ->
            order?.toOrder()
        }
    }

    override suspend fun createOrder(order: Order) {
        val orderEntity = OrderEntity.fromOrder(order)
        orderDao.insertOrder(orderEntity)

        val orderItems = order.items.map { item ->
            OrderItemEntity.fromOrderItem(order.id, item)
        }
        orderDao.insertOrderItems(orderItems)
    }

    override suspend fun updateOrderStatus(id: String, status: OrderStatus) {
        orderDao.updateOrderStatus(id, status)
    }

    override suspend fun cancelOrder(id: String) {
        orderDao.updateOrderStatus(id, OrderStatus.CANCELLED)
    }
} 