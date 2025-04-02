package com.example.shopapp.data.repositories

import com.example.shopapp.data.database.CategoryDao
import com.example.shopapp.data.models.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface CategoryRepository {
    fun getCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: String): Category?
    suspend fun addCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
}

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    
    override fun getCategories(): Flow<List<Category>> = categoryDao.getCategories()
    
    override suspend fun getCategoryById(id: String): Category? = withContext(Dispatchers.IO) {
        categoryDao.getCategoryById(id)
    }
    
    override suspend fun addCategory(category: Category) = withContext(Dispatchers.IO) {
        categoryDao.insertCategory(category)
    }
    
    override suspend fun updateCategory(category: Category) = withContext(Dispatchers.IO) {
        categoryDao.updateCategory(category)
    }
    
    override suspend fun deleteCategory(category: Category) = withContext(Dispatchers.IO) {
        categoryDao.deleteCategory(category)
    }
} 