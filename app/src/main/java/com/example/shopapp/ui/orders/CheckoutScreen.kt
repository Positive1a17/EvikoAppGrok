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
import com.example.shopapp.domain.model.Address
import com.example.shopapp.domain.model.CartItem
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    cartItems: List<CartItem>,
    addresses: List<Address>,
    selectedAddressId: String?,
    onAddressSelect: (String) -> Unit,
    onBackClick: () -> Unit,
    onPlaceOrder: () -> Unit,
    isLoading: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Оформление заказа") },
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
                Text(
                    text = "Товары",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(cartItems) { item ->
                CheckoutItemCard(item = item)
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
            }

            items(addresses) { address ->
                AddressSelectionCard(
                    address = address,
                    isSelected = address.id == selectedAddressId,
                    onClick = { onAddressSelect(address.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Divider(modifier = Modifier.padding(vertical = 16.dp))
            }

            item {
                Text(
                    text = "Итого",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                TotalCard(items = cartItems)
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Button(
                    onClick = onPlaceOrder,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = selectedAddressId != null && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Оформить заказ")
                    }
                }
            }
        }
    }
}

@Composable
fun CheckoutItemCard(item: CartItem) {
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
                    text = "${item.quantity} шт. × ${item.product.price} ₽",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "${item.quantity * item.product.price} ₽",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSelectionCard(
    address: Address,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
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
            if (address.isDefault) {
                Text(
                    text = "По умолчанию",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun TotalCard(items: List<CartItem>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val subtotal = items.sumOf { it.quantity * it.product.price }
            val delivery = 300.0
            val total = subtotal + delivery

            InfoRow("Подытог", "${subtotal} ₽")
            InfoRow("Доставка", "${delivery} ₽")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            InfoRow("Итого", "${total} ₽", MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, style: TextStyle = MaterialTheme.typography.bodyMedium) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = style
        )
        Text(
            text = value,
            style = style
        )
    }
} 