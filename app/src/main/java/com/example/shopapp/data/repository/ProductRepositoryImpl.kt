package com.example.shopapp.data.repository

import com.example.shopapp.data.local.dao.ProductDao
import com.example.shopapp.data.local.entity.ProductEntity
import com.example.shopapp.domain.model.Product
import com.example.shopapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {

    override fun getProducts(): Flow<List<Product>> {
        return productDao.getAllProducts()
            .map { entities -> entities.map { it.toProduct() } }
    }

    override fun getProductById(id: String): Flow<Product?> {
        return productDao.getProductById(id)
            .map { entity -> entity?.toProduct() }
    }

    override fun getProductsByCategory(category: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(category)
            .map { entities -> entities.map { it.toProduct() } }
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query)
            .map { entities -> entities.map { it.toProduct() } }
    }

    override suspend fun addProduct(product: Product) {
        productDao.insertProduct(ProductEntity.fromProduct(product))
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(ProductEntity.fromProduct(product))
    }

    override suspend fun deleteProduct(id: String) {
        productDao.deleteProductById(id)
    }
} 