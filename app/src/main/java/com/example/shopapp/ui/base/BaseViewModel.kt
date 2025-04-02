package com.example.shopapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Базовый класс ViewModel для работы с состояниями и событиями
 */
abstract class BaseViewModel<S, E> : ViewModel() {
    private val initialState: S by lazy { createInitialState() }
    
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()
    
    /**
     * Создает начальное состояние для ViewModel
     */
    abstract fun createInitialState(): S
    
    /**
     * Обрабатывает событие
     */
    abstract fun onEvent(event: E)
    
    /**
     * Обновляет состояние
     */
    protected fun setState(reduce: S.() -> S) {
        _state.value = _state.value.reduce()
    }
    
    /**
     * Запускает корутину в контексте viewModelScope
     */
    protected fun launch(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()
        }
    }
} 