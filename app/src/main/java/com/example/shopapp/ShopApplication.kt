package com.example.shopapp

import android.app.Application
import com.example.shopapp.utils.DatabaseInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ShopApplication : Application() {

    @Inject
    lateinit var databaseInitializer: DatabaseInitializer

    override fun onCreate() {
        super.onCreate()
        
        // Инициализация базы данных тестовыми данными
        databaseInitializer.initDatabase()
    }
} 