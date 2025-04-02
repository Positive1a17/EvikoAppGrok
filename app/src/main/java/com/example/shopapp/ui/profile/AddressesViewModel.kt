package com.example.shopapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.domain.model.Address
import com.example.shopapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressesViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _addresses = MutableStateFlow<List<Address>>(emptyList())
    val addresses: StateFlow<List<Address>> = _addresses.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAddresses()
    }

    private fun loadAddresses() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                userRepository.getCurrentUser()?.let { user ->
                    _addresses.value = user.addresses
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addAddress(address: Address) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                userRepository.getCurrentUser()?.let { user ->
                    userRepository.addUserAddress(user.id, address)
                    loadAddresses()
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateAddress(address: Address) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                userRepository.getCurrentUser()?.let { user ->
                    userRepository.updateUserAddresses(
                        userId = user.id,
                        addresses = _addresses.value.map { 
                            if (it.id == address.id) address else it 
                        }
                    )
                    loadAddresses()
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteAddress(address: Address) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                userRepository.getCurrentUser()?.let { user ->
                    userRepository.deleteUserAddress(user.id, address.id)
                    loadAddresses()
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setDefaultAddress(address: Address) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                userRepository.getCurrentUser()?.let { user ->
                    userRepository.updateDefaultAddress(user.id, address.id)
                    loadAddresses()
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
} 