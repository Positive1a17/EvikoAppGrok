package com.example.shopapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopapp.data.models.Category
import com.example.shopapp.data.models.Product
import com.example.shopapp.data.repositories.CategoryRepository
import com.example.shopapp.data.repositories.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    init {
        loadCategories()
        loadProducts()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                categoryRepository.getCategories().collect { 
                    _categories.value = it
                    if (_selectedCategory.value == null && it.isNotEmpty()) {
                        _selectedCategory.value = it.first().id
                    }
                }
            } catch (e: Exception) {
                // Обработка ошибок
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadProducts(categoryId: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val category = categoryId ?: _selectedCategory.value
                if (category != null) {
                    productRepository.getProductsByCategory(category).collect {
                        _products.value = it
                    }
                } else {
                    productRepository.getProducts().collect {
                        _products.value = it
                    }
                }
            } catch (e: Exception) {
                // Обработка ошибок
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectCategory(categoryId: String) {
        _selectedCategory.value = categoryId
        loadProducts(categoryId)
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                loadProducts()
            } else {
                productRepository.searchProducts(query).collect {
                    _products.value = it
                }
            }
        }
    }
} 