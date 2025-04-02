package com.example.shopapp.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var isDarkMode by remember { mutableStateOf(false) }
    var isNotificationsEnabled by remember { mutableStateOf(true) }
    var selectedLanguage by remember { mutableStateOf("ru") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Настройки",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Appearance
                ListItem(
                    headlineContent = { Text("Внешний вид") },
                    leadingContent = { Icon(Icons.Default.Palette, contentDescription = null) },
                    trailingContent = {
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { isDarkMode = it }
                        )
                    }
                )
                Divider()
                
                // Notifications
                ListItem(
                    headlineContent = { Text("Уведомления") },
                    leadingContent = { Icon(Icons.Default.Notifications, contentDescription = null) },
                    trailingContent = {
                        Switch(
                            checked = isNotificationsEnabled,
                            onCheckedChange = { isNotificationsEnabled = it }
                        )
                    }
                )
                Divider()

                // Language
                ListItem(
                    headlineContent = { Text("Язык") },
                    leadingContent = { Icon(Icons.Default.Language, contentDescription = null) },
                    trailingContent = {
                        DropdownMenu(
                            expanded = false,
                            onDismissRequest = { }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Русский") },
                                onClick = { selectedLanguage = "ru" }
                            )
                            DropdownMenuItem(
                                text = { Text("English") },
                                onClick = { selectedLanguage = "en" }
                            )
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Privacy
                ListItem(
                    headlineContent = { Text("Конфиденциальность") },
                    leadingContent = { Icon(Icons.Default.Security, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.clickable { /* TODO: Navigate to privacy settings */ }
                )
                Divider()

                // Data
                ListItem(
                    headlineContent = { Text("Данные") },
                    leadingContent = { Icon(Icons.Default.Storage, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.clickable { /* TODO: Navigate to data settings */ }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // About
                ListItem(
                    headlineContent = { Text("О приложении") },
                    leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                    trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                    modifier = Modifier.clickable { /* TODO: Navigate to about screen */ }
                )
                Divider()

                // Version
                ListItem(
                    headlineContent = { Text("Версия") },
                    supportingContent = { Text("1.0.0") },
                    leadingContent = { Icon(Icons.Default.Code, contentDescription = null) }
                )
            }
        }
    }
} 