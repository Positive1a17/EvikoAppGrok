package com.example.shopapp.ui.screens.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.data.models.CartItem
import com.example.shopapp.data.repositories.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _orderCompleted = MutableStateFlow(false)
    val orderCompleted: StateFlow<Boolean> = _orderCompleted.asStateFlow()

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone.asStateFlow()

    private val _address = MutableStateFlow("")
    val address: StateFlow<String> = _address.asStateFlow()

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                cartRepository.getCartItems().collect {
                    _cartItems.value = it
                    calculateTotalPrice()
                }
            } catch (e: Exception) {
                // Обработка ошибок
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun calculateTotalPrice() {
        _totalPrice.value = _cartItems.value.sumOf { it.price * it.quantity }
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setPhone(phone: String) {
        _phone.value = phone
    }

    fun setAddress(address: String) {
        _address.value = address
    }

    fun placeOrder() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // В реальном приложении здесь должно быть сохранение заказа в базе данных или отправка на сервер
                // Для примера просто очищаем корзину и устанавливаем флаг завершения заказа
                cartRepository.clearCart()
                _orderCompleted.value = true
            } catch (e: Exception) {
                // Обработка ошибок
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun isFormValid(): Boolean {
        return _name.value.isNotBlank() && 
               _phone.value.isNotBlank() && 
               _address.value.isNotBlank() &&
               _cartItems.value.isNotEmpty()
    }
} 