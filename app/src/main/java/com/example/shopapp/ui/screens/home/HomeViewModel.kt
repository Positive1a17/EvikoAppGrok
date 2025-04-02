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

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    init {
        loadCategories()
        loadProducts()
    }

    fun loadCategories() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                categoryRepository.getCategories().collect { categories ->
                    _categories.value = categories
                    if (_selectedCategory.value == null && categories.isNotEmpty()) {
                        _selectedCategory.value = categories.first().id
                    }
                }
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки категорий: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadProducts(categoryId: String? = _selectedCategory.value) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val products = if (categoryId != null) {
                    productRepository.getProductsByCategory(categoryId)
                } else {
                    productRepository.getAllProducts()
                }
                products.collect { productList ->
                    _products.value = productList
                }
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки товаров: ${e.message}"
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