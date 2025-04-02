package com.example.shopapp.utils

import com.example.shopapp.data.models.Category
import com.example.shopapp.data.models.Product
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Класс для генерации тестовых данных для приложения
 */
@Singleton
class DataGenerator @Inject constructor(
    private val securityUtils: SecurityUtils
) {
    
    /**
     * Генерирует список категорий
     */
    fun generateCategories(): List<Category> {
        return listOf(
            Category(
                id = "cat_1",
                name = "Смартфоны",
                iconUrl = "https://source.unsplash.com/random/300x300/?smartphone",
                order = 1
            ),
            Category(
                id = "cat_2",
                name = "Ноутбуки",
                iconUrl = "https://source.unsplash.com/random/300x300/?laptop",
                order = 2
            ),
            Category(
                id = "cat_3",
                name = "Планшеты",
                iconUrl = "https://source.unsplash.com/random/300x300/?tablet",
                order = 3
            ),
            Category(
                id = "cat_4",
                name = "Аксессуары",
                iconUrl = "https://source.unsplash.com/random/300x300/?accessories",
                order = 4
            ),
            Category(
                id = "cat_5",
                name = "Наушники",
                iconUrl = "https://source.unsplash.com/random/300x300/?headphones",
                order = 5
            )
        )
    }
    
    /**
     * Генерирует список продуктов
     */
    fun generateProducts(): List<Product> {
        return listOf(
            // Смартфоны
            Product(
                id = "prod_1",
                name = "Смартфон Galaxy S21",
                description = "Флагманский смартфон с мощным процессором и отличной камерой",
                price = 59990.0,
                imageUrl = "https://source.unsplash.com/random/600x400/?samsung",
                modelUrl = "models/smartphone.glb",
                categoryId = "cat_1"
            ),
            Product(
                id = "prod_2",
                name = "iPhone 13 Pro",
                description = "Мощный процессор A15 Bionic, улучшенная система камер",
                price = 89990.0,
                imageUrl = "https://source.unsplash.com/random/600x400/?iphone",
                modelUrl = "models/iphone.glb",
                categoryId = "cat_1"
            ),
            Product(
                id = "prod_3",
                name = "Xiaomi Mi 11",
                description = "Флагманский смартфон с процессором Snapdragon 888",
                price = 49990.0,
                imageUrl = "https://source.unsplash.com/random/600x400/?xiaomi",
                modelUrl = "models/smartphone.glb",
                categoryId = "cat_1"
            ),
            
            // Ноутбуки
            Product(
                id = "prod_4",
                name = "MacBook Pro 16'",
                description = "Мощный ноутбук для профессионалов с чипом M1 Pro",
                price = 189990.0,
                imageUrl = "https://source.unsplash.com/random/600x400/?macbook",
                modelUrl = "models/laptop.glb",
                categoryId = "cat_2"
            ),
            Product(
                id = "prod_5",
                name = "Dell XPS 15",
                description = "Премиальный ноутбук с InfinityEdge дисплеем",
                price = 149990.0,
                imageUrl = "https://source.unsplash.com/random/600x400/?dell",
                modelUrl = "models/laptop.glb",
                categoryId = "cat_2"
            ),
            
            // Планшеты
            Product(
                id = "prod_6",
                name = "iPad Pro 12.9'",
                description = "Самый мощный планшет с дисплеем Liquid Retina XDR",
                price = 99990.0,
                imageUrl = "https://source.unsplash.com/random/600x400/?ipad",
                modelUrl = "models/tablet.glb",
                categoryId = "cat_3"
            ),
            Product(
                id = "prod_7",
                name = "Samsung Galaxy Tab S7+",
                description = "Флагманский планшет с AMOLED дисплеем",
                price = 79990.0,
                imageUrl = "https://source.unsplash.com/random/600x400/?tablet",
                modelUrl = "models/tablet.glb",
                categoryId = "cat_3"
            ),
            
            // Аксессуары
            Product(
                id = "prod_8",
                name = "Чехол для смартфона",
                description = "Защитный чехол с красивым дизайном",
                price = 1990.0,
                imageUrl = "https://source.unsplash.com/random/600x400/?phone_case",
                modelUrl = "models/case.glb",
                categoryId = "cat_4"
            ),
            Product(
                id = "prod_9",
                name = "Зарядное устройство",
                description = "Быстрая зарядка мощностью 65Вт",
                price = 2990.0,
                imageUrl = "https://source.unsplash.com/random/600x400/?charger",
                modelUrl = "models/charger.glb",
                categoryId = "cat_4"
            ),
            
            // Наушники
            Product(
                id = "prod_10",
                name = "AirPods Pro",
                description = "Беспроводные наушники с активным шумоподавлением",
                price = 19990.0,
                imageUrl = "https://source.unsplash.com/random/600x400/?airpods",
                modelUrl = "models/headphones.glb",
                categoryId = "cat_5"
            ),
            Product(
                id = "prod_11",
                name = "Sony WH-1000XM4",
                description = "Наушники с лучшим активным шумоподавлением",
                price = 27990.0,
                imageUrl = "https://source.unsplash.com/random/600x400/?headphones",
                modelUrl = "models/headphones.glb",
                categoryId = "cat_5"
            )
        )
    }
    
    /**
     * Генерирует тестового пользователя
     */
    fun generateTestUser(): com.example.shopapp.data.models.User {
        return com.example.shopapp.data.models.User(
            id = securityUtils.generateUUID(),
            email = "test@example.com",
            passwordHash = securityUtils.hashPassword("password"),
            role = "user",
            securityCode = securityUtils.hashPassword("1234")
        )
    }
} 