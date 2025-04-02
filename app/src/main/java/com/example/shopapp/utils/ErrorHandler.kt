package com.example.shopapp.utils

import android.content.Context
import com.example.shopapp.R
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ErrorHandler @Inject constructor(
    private val context: Context
) {
    fun getErrorMessage(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> {
                when (throwable.code()) {
                    401 -> "Ошибка авторизации"
                    403 -> "Доступ запрещен"
                    404 -> "Ресурс не найден"
                    500 -> "Ошибка сервера"
                    else -> "Неизвестная ошибка сети"
                }
            }
            is IOException -> "Ошибка сети"
            is SocketTimeoutException -> "Превышено время ожидания"
            is UnknownHostException -> "Нет подключения к интернету"
            else -> "Произошла ошибка: ${throwable.message}"
        }
    }

    sealed class Error {
        object Network : Error()
        object NotFound : Error()
        object AccessDenied : Error()
        object ServiceUnavailable : Error()
        data class Unknown(val message: String) : Error()
        data class Api(val code: Int, val message: String) : Error()
    }
} 