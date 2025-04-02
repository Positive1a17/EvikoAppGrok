package com.example.shopapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shopapp.data.local.dao.CartDao
import com.example.shopapp.data.local.dao.ProductDao
import com.example.shopapp.data.local.dao.UserDao
import com.example.shopapp.data.local.entity.CartItemEntity
import com.example.shopapp.data.local.entity.ProductEntity
import com.example.shopapp.data.local.entity.UserEntity
import com.example.shopapp.data.local.util.Converters

@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        UserEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun userDao(): UserDao
} 