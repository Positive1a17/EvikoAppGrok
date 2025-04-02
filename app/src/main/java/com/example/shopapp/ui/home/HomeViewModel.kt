package com.example.shopapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.domain.model.Product
import com.example.shopapp.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("all")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    init {
        loadProducts()
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            loadProducts()
        } else {
            searchProducts(query)
        }
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
        if (category == "all") {
            loadProducts()
        } else {
            loadProductsByCategory(category)
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            productRepository.getProducts()
                .catch { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Неизвестная ошибка")
                }
                .collect { products ->
                    _uiState.value = HomeUiState.Success(products)
                }
        }
    }

    private fun searchProducts(query: String) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            productRepository.searchProducts(query)
                .catch { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Неизвестная ошибка")
                }
                .collect { products ->
                    _uiState.value = HomeUiState.Success(products)
                }
        }
    }

    private fun loadProductsByCategory(category: String) {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            productRepository.getProductsByCategory(category)
                .catch { error ->
                    _uiState.value = HomeUiState.Error(error.message ?: "Неизвестная ошибка")
                }
                .collect { products ->
                    _uiState.value = HomeUiState.Success(products)
                }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val products: List<Product>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
} 