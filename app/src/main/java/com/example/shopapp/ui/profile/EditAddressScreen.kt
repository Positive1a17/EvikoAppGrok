package com.example.shopapp.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shopapp.domain.model.Address

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddressScreen(
    address: Address?,
    onBackClick: () -> Unit,
    onSaveClick: (Address) -> Unit,
    isLoading: Boolean
) {
    var street by remember { mutableStateOf(address?.street ?: "") }
    var city by remember { mutableStateOf(address?.city ?: "") }
    var postalCode by remember { mutableStateOf(address?.postalCode ?: "") }
    var country by remember { mutableStateOf(address?.country ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (address == null) "Новый адрес" else "Редактировать адрес") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = street,
                onValueChange = { street = it },
                label = { Text("Улица") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Город") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = postalCode,
                onValueChange = { postalCode = it },
                label = { Text("Почтовый индекс") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = country,
                onValueChange = { country = it },
                label = { Text("Страна") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onSaveClick(
                        Address(
                            id = address?.id ?: "",
                            street = street,
                            city = city,
                            postalCode = postalCode,
                            country = country,
                            isDefault = address?.isDefault ?: false
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && street.isNotBlank() && city.isNotBlank() && 
                         postalCode.isNotBlank() && country.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Сохранить")
                }
            }
        }
    }
} 