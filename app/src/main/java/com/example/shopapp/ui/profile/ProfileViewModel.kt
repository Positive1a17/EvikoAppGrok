package com.example.shopapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.domain.model.User
import com.example.shopapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _user.value = userRepository.getCurrentUser()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                userRepository.updateUser(user)
                _user.value = user
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    fun updateUserAddresses(userId: String, addresses: List<Address>) {
        viewModelScope.launch {
            try {
                userRepository.updateUserAddresses(userId, addresses)
            } catch (error: Exception) {
                _error.value = error.message
            }
        }
    }

    fun addUserAddress(userId: String, address: Address) {
        viewModelScope.launch {
            try {
                userRepository.addUserAddress(userId, address)
            } catch (error: Exception) {
                _uiState.value = ProfileUiState.Error(error.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun updateDefaultAddress(userId: String, addressId: String) {
        viewModelScope.launch {
            try {
                userRepository.updateDefaultAddress(userId, addressId)
            } catch (error: Exception) {
                _uiState.value = ProfileUiState.Error(error.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun deleteUserAddress(userId: String, addressId: String) {
        viewModelScope.launch {
            try {
                userRepository.deleteUserAddress(userId, addressId)
            } catch (error: Exception) {
                _uiState.value = ProfileUiState.Error(error.message ?: "Неизвестная ошибка")
            }
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            userRepository.getCurrentUser()
                .catch { error ->
                    _uiState.value = ProfileUiState.Error(error.message ?: "Неизвестная ошибка")
                }
                .collect { user ->
                    _uiState.value = if (user != null) {
                        ProfileUiState.Success(user)
                    } else {
                        ProfileUiState.NotAuthenticated
                    }
                }
        }
    }
}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: User) : ProfileUiState()
    object NotAuthenticated : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
} 