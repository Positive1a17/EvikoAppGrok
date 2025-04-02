package com.example.shopapp.utils

object Constants {
    // API
    const val BASE_URL = "https://api.example.com"
    const val API_TIMEOUT = 30L
    const val API_RETRY_COUNT = 3

    // База данных
    const val DATABASE_NAME = "shop_database"
    const val DATABASE_VERSION = 1

    // Настройки
    const val PREFERENCES_NAME = "shop_preferences"
    const val KEY_THEME_MODE = "theme_mode"
    const val KEY_LANGUAGE = "language"
    const val KEY_NOTIFICATIONS = "notifications_enabled"

    // Аутентификация
    const val AUTH_TOKEN_KEY = "auth_token"
    const val REFRESH_TOKEN_KEY = "refresh_token"
    const val TOKEN_EXPIRY_KEY = "token_expiry"

    // Навигация
    const val DEEP_LINK_SCHEME = "shopapp"
    const val DEEP_LINK_HOST = "example.com"

    // Кэширование
    const val CACHE_SIZE = 50 * 1024 * 1024L // 50 MB
    const val CACHE_TIMEOUT = 24 * 60 * 60 * 1000L // 24 часа

    // Другие
    const val MIN_PASSWORD_LENGTH = 8
    const val MAX_PRODUCT_QUANTITY = 99
    const val DEFAULT_PAGE_SIZE = 20
} 