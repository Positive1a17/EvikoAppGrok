package com.example.shopapp.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shopapp.domain.model.Address

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressesScreen(
    addresses: List<Address>,
    onBackClick: () -> Unit,
    onAddAddressClick: () -> Unit,
    onEditAddressClick: (Address) -> Unit,
    onDeleteAddressClick: (Address) -> Unit,
    onSetDefaultAddressClick: (Address) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои адреса") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddAddressClick) {
                Icon(Icons.Default.Add, contentDescription = "Добавить адрес")
            }
        }
    ) { padding ->
        if (addresses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "У вас пока нет адресов",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(addresses) { address ->
                    AddressCard(
                        address = address,
                        onEditClick = { onEditAddressClick(address) },
                        onDeleteClick = { onDeleteAddressClick(address) },
                        onSetDefaultClick = { onSetDefaultAddressClick(address) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressCard(
    address: Address,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSetDefaultClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${address.street}, ${address.city}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${address.postalCode}, ${address.country}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (!address.isDefault) {
                    TextButton(onClick = onSetDefaultClick) {
                        Text("Сделать основным")
                    }
                }
            }

            if (address.isDefault) {
                Text(
                    text = "Основной адрес",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEditClick) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Редактировать")
                }
                TextButton(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Удалить")
                }
            }
        }
    }
} 