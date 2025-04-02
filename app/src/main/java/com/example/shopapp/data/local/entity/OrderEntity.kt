package com.example.shopapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shopapp.domain.model.Order
import com.example.shopapp.domain.model.OrderStatus
import java.util.Date

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val totalPrice: Double,
    val status: OrderStatus,
    val deliveryAddressId: String,
    val createdAt: Date,
    val updatedAt: Date
) {
    fun toOrder(items: List<OrderItemEntity> = emptyList()): Order {
        return Order(
            id = id,
            userId = userId,
            items = items.map { it.toOrderItem() },
            totalPrice = totalPrice,
            status = status,
            deliveryAddress = AddressEntity(
                id = deliveryAddressId,
                userId = userId,
                street = "",
                city = "",
                postalCode = "",
                country = "",
                isDefault = false
            ).toAddress(),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        fun fromOrder(order: Order): OrderEntity {
            return OrderEntity(
                id = order.id,
                userId = order.userId,
                totalPrice = order.totalPrice,
                status = order.status,
                deliveryAddressId = order.deliveryAddress.id,
                createdAt = order.createdAt,
                updatedAt = order.updatedAt
            )
        }
    }
}

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class OrderItemEntity(
    @PrimaryKey
    val id: String,
    val orderId: String,
    val productId: String,
    val quantity: Int,
    val price: Double
) {
    fun toOrderItem(product: ProductEntity? = null): OrderItem {
        return OrderItem(
            product = product?.toProduct() ?: Product(
                id = productId,
                name = "",
                description = "",
                price = price,
                category = "",
                imageUrl = null,
                specifications = emptyMap()
            ),
            quantity = quantity,
            price = price
        )
    }

    companion object {
        fun fromOrderItem(orderId: String, orderItem: OrderItem): OrderItemEntity {
            return OrderItemEntity(
                id = java.util.UUID.randomUUID().toString(),
                orderId = orderId,
                productId = orderItem.product.id,
                quantity = orderItem.quantity,
                price = orderItem.price
            )
        }
    }
} 