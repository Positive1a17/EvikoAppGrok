package com.example.shopapp.ui.screens.login

import androidx.lifecycle.viewModelScope
import com.example.shopapp.data.repositories.AuthRepository
import com.example.shopapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel<LoginState, LoginEvent>() {

    override fun createInitialState(): LoginState = LoginState()

    override fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> setState { copy(email = event.email) }
            is LoginEvent.PasswordChanged -> setState { copy(password = event.password) }
            is LoginEvent.Login -> login()
        }
    }

    private fun login() {
        setState { copy(isLoading = true, error = null) }
        launch {
            val result = authRepository.login(state.value.email, state.value.password)
            setState { 
                copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message,
                    isLoggedIn = result.isSuccess
                )
            }
        }
    }
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object Login : LoginEvent()
} 