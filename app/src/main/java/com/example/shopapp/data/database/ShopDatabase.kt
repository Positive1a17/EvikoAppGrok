package com.example.shopapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shopapp.data.models.*

@Database(
    entities = [
        User::class,
        Product::class,
        Category::class,
        CartItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ShopDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun cartItemDao(): CartItemDao
} 