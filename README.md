# EvikoAppGrok

Android-приложение для магазина с 3D-визуализацией товаров.

## Описание

EvikoAppGrok - это современное Android-приложение, которое позволяет пользователям просматривать каталог товаров с возможностью 3D-визуализации. Приложение построено с использованием современных технологий и следует лучшим практикам разработки Android-приложений.

## Технологии

- **Kotlin** - основной язык программирования
- **Jetpack Compose** - современный UI-фреймворк
- **Clean Architecture** - архитектурный паттерн
- **MVVM** - паттерн представления
- **Hilt** - внедрение зависимостей
- **Room** - локальная база данных
- **Coroutines & Flow** - асинхронное программирование
- **Filament** - 3D-рендеринг
- **DataStore** - хранение настроек
- **Navigation Component** - навигация

## Основные функции

- 🔐 Аутентификация пользователей
- 📱 Каталог товаров с поиском и фильтрацией
- 🎨 3D-просмотр моделей товаров
- 🛒 Корзина покупок
- 🌓 Поддержка темной/светлой темы
- 🌍 Многоязычность
- 📱 Офлайн-режим
- 🔒 Безопасное хранение данных

## Требования

- Android Studio Hedgehog | 2023.1.1
- Android SDK 34
- JDK 17
- Gradle 8.2
- Минимум 4GB RAM
- GPU с поддержкой OpenGL ES 3.0

## Установка

1. Клонируйте репозиторий:
```bash
git clone https://github.com/Positive1a17/EvikoAppGrok.git
```

2. Откройте проект в Android Studio

3. Синхронизируйте Gradle

4. Запустите приложение

## Структура проекта

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/shopapp/
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   ├── remote/
│   │   │   │   └── repository/
│   │   │   ├── di/
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   └── usecase/
│   │   │   ├── ui/
│   │   │   │   ├── auth/
│   │   │   │   ├── cart/
│   │   │   │   ├── home/
│   │   │   │   ├── product/
│   │   │   │   └── settings/
│   │   │   └── utils/
│   │   └── res/
│   └── test/
└── build.gradle.kts
```

## Тестирование

### Unit-тесты
```bash
./gradlew test
```

### Инструментальные тесты
```bash
./gradlew connectedAndroidTest
```

## Устранение неполадок

### Проблемы с 3D-моделями
1. Проверьте поддержку OpenGL ES 3.0 на устройстве
2. Убедитесь, что модель загружена корректно
3. Проверьте свободное место на устройстве

### Проблемы с авторизацией
1. Проверьте подключение к интернету
2. Очистите кэш приложения
3. Перезапустите приложение

### Проблемы с производительностью
1. Закройте фоновые приложения
2. Очистите кэш
3. Обновите приложение до последней версии

## API Документация

### Основные классы

#### BaseViewModel
```kotlin
abstract class BaseViewModel<E : Any, S : Any> : ViewModel() {
    protected abstract fun createInitialState(): S
    protected abstract suspend fun onEvent(event: E)
    protected fun setState(reduce: S.() -> S)
    protected fun launch(block: suspend () -> Unit)
}
```

#### SettingsManager
```kotlin
class SettingsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val themeMode: Flow<String>
    val language: Flow<String>
    val notificationsEnabled: Flow<Boolean>
    
    suspend fun setThemeMode(mode: String)
    suspend fun setLanguage(language: String)
    suspend fun setNotificationsEnabled(enabled: Boolean)
}
```

## Контрибьютинг

1. Форкните репозиторий
2. Создайте ветку для вашей функции (`git checkout -b feature/amazing-feature`)
3. Зафиксируйте изменения (`git commit -m 'Add some amazing feature'`)
4. Отправьте изменения в ветку (`git push origin feature/amazing-feature`)
5. Откройте Pull Request

## Стандарты кодирования

- Используйте Kotlin Coding Conventions
- Следуйте принципам Clean Architecture
- Документируйте публичные API
- Пишите unit-тесты для бизнес-логики
- Используйте meaningful имена для переменных и функций

## Лицензия

Этот проект распространяется под лицензией MIT. См. файл [LICENSE](LICENSE) для более подробной информации.

## Контакты

- Email: eviko1a17@gmail.com
