package com.example.shopapp.ui.screens.login

import com.example.shopapp.data.models.User
import com.example.shopapp.data.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private lateinit var viewModel: LoginViewModel
    private lateinit var authRepository: AuthRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mock()
        viewModel = LoginViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when email changed, state is updated`() = runTest {
        val email = "test@example.com"
        viewModel.onEvent(LoginEvent.EmailChanged(email))
        
        assertEquals(email, viewModel.state.first().email)
    }

    @Test
    fun `when password changed, state is updated`() = runTest {
        val password = "password123"
        viewModel.onEvent(LoginEvent.PasswordChanged(password))
        
        assertEquals(password, viewModel.state.first().password)
    }

    @Test
    fun `when login successful, state is updated correctly`() = runTest {
        val email = "test@example.com"
        val password = "password123"
        val user = User("1", email, "hashedPassword", "user")
        
        whenever(authRepository.login(email, password))
            .thenReturn(Result.success(user))

        viewModel.onEvent(LoginEvent.EmailChanged(email))
        viewModel.onEvent(LoginEvent.PasswordChanged(password))
        viewModel.onEvent(LoginEvent.Login)
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        with(viewModel.state.first()) {
            assertTrue(isLoggedIn)
            assertFalse(isLoading)
            assertNull(error)
        }
        
        verify(authRepository).login(email, password)
    }

    @Test
    fun `when login fails, state contains error`() = runTest {
        val email = "test@example.com"
        val password = "wrong_password"
        val errorMessage = "Неверный email или пароль"
        
        whenever(authRepository.login(email, password))
            .thenReturn(Result.failure(Exception(errorMessage)))

        viewModel.onEvent(LoginEvent.EmailChanged(email))
        viewModel.onEvent(LoginEvent.PasswordChanged(password))
        viewModel.onEvent(LoginEvent.Login)
        
        testDispatcher.scheduler.advanceUntilIdle()
        
        with(viewModel.state.first()) {
            assertFalse(isLoggedIn)
            assertFalse(isLoading)
            assertEquals(errorMessage, error)
        }
        
        verify(authRepository).login(email, password)
    }
} 