package com.example.shopapp.ui.screens.security

import androidx.lifecycle.viewModelScope
import com.example.shopapp.data.repositories.AuthRepository
import com.example.shopapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecurityCodeViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel<SecurityCodeState, SecurityCodeEvent>() {

    private val _securityCode = MutableStateFlow("")
    val securityCode = _securityCode.asStateFlow()

    override fun createInitialState(): SecurityCodeState = SecurityCodeState()

    init {
        generateSecurityCode()
    }

    override fun onEvent(event: SecurityCodeEvent) {
        when (event) {
            is SecurityCodeEvent.CodeChanged -> setState { copy(code = event.code) }
            is SecurityCodeEvent.VerifyCode -> verifyCode()
        }
    }

    private fun generateSecurityCode() {
        setState { copy(isLoading = true, error = null) }
        launch {
            val user = authRepository.getCurrentUser()
            if (user != null) {
                val result = authRepository.generateSecurityCode(user.id)
                result.onSuccess { code ->
                    _securityCode.value = code
                    setState { copy(isLoading = false) }
                }
                result.onFailure { error ->
                    setState { copy(isLoading = false, error = error.message) }
                }
            } else {
                setState { copy(isLoading = false, error = "Пользователь не авторизован") }
            }
        }
    }

    private fun verifyCode() {
        setState { copy(isLoading = true, error = null) }
        launch {
            val user = authRepository.getCurrentUser()
            if (user != null) {
                val result = authRepository.verifySecurityCode(user.id, state.value.code)
                result.onSuccess { isVerified ->
                    setState { copy(isLoading = false, isVerified = isVerified) }
                }
                result.onFailure { error ->
                    setState { copy(isLoading = false, error = error.message) }
                }
            } else {
                setState { copy(isLoading = false, error = "Пользователь не авторизован") }
            }
        }
    }
}

data class SecurityCodeState(
    val code: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isVerified: Boolean = false
)

sealed class SecurityCodeEvent {
    data class CodeChanged(val code: String) : SecurityCodeEvent()
    object VerifyCode : SecurityCodeEvent()
} 