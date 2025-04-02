package com.example.shopapp.utils

import com.example.shopapp.data.database.CartItemDao
import com.example.shopapp.data.database.CategoryDao
import com.example.shopapp.data.database.ProductDao
import com.example.shopapp.data.database.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс для инициализации базы данных тестовыми данными
 */
@Singleton
class DatabaseInitializer @Inject constructor(
    private val userDao: UserDao,
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao,
    private val cartItemDao: CartItemDao,
    private val dataGenerator: DataGenerator
) {
    
    /**
     * Инициализирует базу данных тестовыми данными,
     * если база данных пуста
     */
    fun initDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            // Инициализация категорий
            if (categoryDao.getCategoriesCount() == 0) {
                dataGenerator.generateCategories().forEach { category ->
                    categoryDao.insertCategory(category)
                }
            }
            
            // Инициализация продуктов
            if (productDao.getProductsCount() == 0) {
                dataGenerator.generateProducts().forEach { product ->
                    productDao.insertProduct(product)
                }
            }
            
            // Инициализация тестового пользователя
            if (userDao.getUsersCount() == 0) {
                userDao.insertUser(dataGenerator.generateTestUser())
            }
        }
    }
}