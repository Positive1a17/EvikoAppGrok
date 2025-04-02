package com.example.shopapp.ui.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.domain.model.Product
import com.example.shopapp.domain.repository.ProductRepository
import com.example.shopapp.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = checkNotNull(savedStateHandle["productId"])

    private val _uiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    init {
        loadProduct()
    }

    fun incrementQuantity() {
        _quantity.value++
    }

    fun decrementQuantity() {
        if (_quantity.value > 1) {
            _quantity.value--
        }
    }

    fun toggleFavorite() {
        _isFavorite.value = !_isFavorite.value
        // TODO: Save to DataStore
    }

    fun addToCart() {
        viewModelScope.launch {
            try {
                cartRepository.addToCart(productId, _quantity.value)
            } catch (error: Exception) {
                _uiState.value = ProductDetailUiState.Error(error.message ?: "Неизвестная ошибка")
            }
        }
    }

    private fun loadProduct() {
        viewModelScope.launch {
            _uiState.value = ProductDetailUiState.Loading
            productRepository.getProductById(productId)
                .catch { error ->
                    _uiState.value = ProductDetailUiState.Error(error.message ?: "Неизвестная ошибка")
                }
                .collect { product ->
                    _uiState.value = if (product != null) {
                        ProductDetailUiState.Success(product)
                    } else {
                        ProductDetailUiState.Error("Товар не найден")
                    }
                }
        }
    }
}

sealed class ProductDetailUiState {
    object Loading : ProductDetailUiState()
    data class Success(val product: Product) : ProductDetailUiState()
    data class Error(val message: String) : ProductDetailUiState()
} 