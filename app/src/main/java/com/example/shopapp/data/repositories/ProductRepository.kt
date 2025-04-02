package com.example.shopapp.data.repositories

import com.example.shopapp.data.database.ProductDao
import com.example.shopapp.data.models.Product
import com.example.shopapp.utils.SecurityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun getProductsByCategory(categoryId: String): Flow<List<Product>>
    fun getProduct(id: String): Flow<Product?>
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun addProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
}

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val securityUtils: SecurityUtils
) : ProductRepository {
    
    override fun getProducts(): Flow<List<Product>> = productDao.getProducts()
    
    override fun getProductsByCategory(categoryId: String): Flow<List<Product>> = 
        productDao.getProductsByCategory(categoryId)
    
    override fun getProduct(id: String): Flow<Product?> = productDao.getProductById(id)
    
    override fun searchProducts(query: String): Flow<List<Product>> = productDao.searchProducts(query)
    
    override suspend fun addProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.insertProduct(product)
    }
    
    override suspend fun updateProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.updateProduct(product)
    }
    
    override suspend fun deleteProduct(product: Product) = withContext(Dispatchers.IO) {
        productDao.deleteProduct(product)
    }

    fun getFavoriteProducts(): Flow<List<Product>> = productDao.getFavoriteProducts()

    suspend fun toggleFavorite(productId: String) {
        val product = productDao.getProductById(productId)
        product?.let {
            productDao.updateProduct(it.copy(isFavorite = !it.isFavorite))
        }
    }
} 