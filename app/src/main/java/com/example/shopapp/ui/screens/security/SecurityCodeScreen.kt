package com.example.shopapp.ui.screens.security

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun SecurityCodeScreen(
    navController: NavController,
    viewModel: SecurityCodeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val securityCode by viewModel.securityCode.collectAsState()

    LaunchedEffect(state.isVerified) {
        if (state.isVerified) {
            navController.navigate("home") {
                popUpTo("security_code") { inclusive = true }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Двухфакторная аутентификация",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // В реальном приложении здесь должен быть код для отправки SMS/Email
            // Для демонстрации просто покажем код на экране
            Text(
                text = "Ваш код безопасности: $securityCode",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = state.code,
                onValueChange = { 
                    if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                        viewModel.onEvent(SecurityCodeEvent.CodeChanged(it))
                    }
                },
                label = { Text("Введите 4-значный код") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            if (state.error != null) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Button(
                onClick = { viewModel.onEvent(SecurityCodeEvent.VerifyCode) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading && state.code.length == 4
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Подтвердить")
                }
            }
        }
    }
} 