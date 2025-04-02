package com.example.shopapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _isNotificationsEnabled = MutableStateFlow(true)
    val isNotificationsEnabled: StateFlow<Boolean> = _isNotificationsEnabled.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("ru")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
        // TODO: Save to DataStore
    }

    fun toggleNotifications() {
        _isNotificationsEnabled.value = !_isNotificationsEnabled.value
        // TODO: Save to DataStore
    }

    fun setLanguage(language: String) {
        _selectedLanguage.value = language
        // TODO: Save to DataStore
    }

    fun clearAppData() {
        viewModelScope.launch {
            // TODO: Clear all app data
        }
    }
} 