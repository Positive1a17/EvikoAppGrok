package com.example.shopapp.utils

object Constants {
    // API
    const val BASE_URL = "https://api.example.com/"
    const val API_TIMEOUT = 30L
    const val API_RETRY_COUNT = 3

    // Database
    const val DATABASE_NAME = "shop_database"
    const val DATABASE_VERSION = 1

    // Preferences
    const val PREFERENCES_NAME = "shop_preferences"
    const val KEY_THEME_MODE = "theme_mode"
    const val KEY_LANGUAGE = "language"
    const val KEY_NOTIFICATIONS = "notifications"

    // Authentication
    const val AUTH_TOKEN_KEY = "auth_token"
    const val REFRESH_TOKEN_KEY = "refresh_token"
    const val TOKEN_EXPIRY_KEY = "token_expiry"

    // Navigation
    const val DEEP_LINK_SCHEME = "shopapp"
    const val DEEP_LINK_HOST = "example.com"

    // Cache
    const val CACHE_SIZE = 10 * 1024 * 1024L // 10 MB
    const val CACHE_TIMEOUT = 7 * 24 * 60 * 60L // 1 week

    // Other
    const val MIN_PASSWORD_LENGTH = 8
    const val MAX_PRODUCT_QUANTITY = 99
    const val DEFAULT_PAGE_SIZE = 20
} 