package com.example.shopapp.di

import android.content.Context
import androidx.room.Room
import com.example.shopapp.data.database.ShopDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ShopDatabase = Room.databaseBuilder(
        context,
        ShopDatabase::class.java,
        "shop_database"
    ).build()

    @Provides
    @Singleton
    fun provideUserDao(database: ShopDatabase) = database.userDao()

    @Provides
    @Singleton
    fun provideProductDao(database: ShopDatabase) = database.productDao()

    @Provides
    @Singleton
    fun provideCategoryDao(database: ShopDatabase) = database.categoryDao()

    @Provides
    @Singleton
    fun provideCartItemDao(database: ShopDatabase) = database.cartItemDao()
} 