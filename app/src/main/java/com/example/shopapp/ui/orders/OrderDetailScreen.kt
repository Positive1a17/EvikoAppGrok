package com.example.shopapp.ui.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shopapp.domain.model.Order
import com.example.shopapp.domain.model.OrderStatus
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    order: Order,
    onBackClick: () -> Unit,
    onCancelOrder: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Заказ #${order.id.takeLast(6)}") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                OrderStatusCard(status = order.status)
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "Товары",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(order.items) { item ->
                OrderItemCard(item = item)
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                Text(
                    text = "Адрес доставки",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                AddressCard(address = order.deliveryAddress)
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Text(
                    text = "Информация о заказе",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                OrderInfoCard(order = order)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (order.status == OrderStatus.PENDING) {
                item {
                    Button(
                        onClick = onCancelOrder,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Отменить заказ")
                    }
                }
            }
        }
    }
}

@Composable
fun OrderStatusCard(status: OrderStatus) {
    val (color, text) = when (status) {
        OrderStatus.PENDING -> MaterialTheme.colorScheme.primary to "Ожидает"
        OrderStatus.CONFIRMED -> MaterialTheme.colorScheme.secondary to "Подтвержден"
        OrderStatus.SHIPPED -> MaterialTheme.colorScheme.tertiary to "Отправлен"
        OrderStatus.DELIVERED -> MaterialTheme.colorScheme.primary to "Доставлен"
        OrderStatus.CANCELLED -> MaterialTheme.colorScheme.error to "Отменен"
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = color,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun OrderItemCard(item: OrderItem) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.product.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${item.quantity} шт. × ${item.price} ₽",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "${item.quantity * item.price} ₽",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun AddressCard(address: Address) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "${address.street}, ${address.city}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${address.postalCode}, ${address.country}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun OrderInfoCard(order: Order) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            InfoRow("Дата заказа", formatDate(order.createdAt))
            InfoRow("Дата обновления", formatDate(order.updatedAt))
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            InfoRow("Итого", "${order.totalPrice} ₽")
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.format(date)
} 