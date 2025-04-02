package com.example.shopapp.ui.screens.login

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.shopapp.ui.theme.ShopAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        
        composeTestRule.setContent {
            ShopAppTheme {
                LoginScreen(navController = navController)
            }
        }
    }

    @Test
    fun loginScreen_displaysAllElements() {
        composeTestRule.onNodeWithText("Вход в приложение").assertExists()
        composeTestRule.onNodeWithText("Email").assertExists()
        composeTestRule.onNodeWithText("Пароль").assertExists()
        composeTestRule.onNodeWithText("Войти").assertExists()
    }

    @Test
    fun loginButton_initiallyEnabled() {
        composeTestRule.onNodeWithText("Войти").assertIsEnabled()
    }

    @Test
    fun emailField_updatesCorrectly() {
        val testEmail = "test@example.com"
        composeTestRule.onNodeWithText("Email")
            .performTextInput(testEmail)
        
        composeTestRule.onNodeWithText(testEmail).assertExists()
    }

    @Test
    fun passwordField_updatesCorrectly() {
        val testPassword = "password123"
        composeTestRule.onNodeWithText("Пароль")
            .performTextInput(testPassword)
        
        // Пароль скрыт, поэтому проверяем, что поле не пустое
        composeTestRule.onNodeWithText("Пароль")
            .assertTextContains("•".repeat(testPassword.length))
    }

    @Test
    fun loginButton_disabledDuringLoading() {
        // Вводим данные
        composeTestRule.onNodeWithText("Email")
            .performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Пароль")
            .performTextInput("password123")
        
        // Нажимаем кнопку входа
        composeTestRule.onNodeWithText("Войти").performClick()
        
        // Проверяем, что кнопка неактивна во время загрузки
        composeTestRule.onNodeWithText("Войти").assertIsNotEnabled()
    }

    @Test
    fun errorMessage_displayedOnError() {
        // Вводим неверные данные
        composeTestRule.onNodeWithText("Email")
            .performTextInput("invalid@email.com")
        composeTestRule.onNodeWithText("Пароль")
            .performTextInput("wrong_password")
        
        // Нажимаем кнопку входа
        composeTestRule.onNodeWithText("Войти").performClick()
        
        // Ждем появления сообщения об ошибке
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("Неверный email или пароль")
                .fetchSemanticsNodes().size == 1
        }
    }
} 