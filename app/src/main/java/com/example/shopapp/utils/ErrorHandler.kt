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
                    401 -> context.getString(R.string.error_unauthorized)
                    403 -> context.getString(R.string.error_forbidden)
                    404 -> context.getString(R.string.error_not_found)
                    500 -> context.getString(R.string.error_server)
                    else -> context.getString(R.string.error_unknown)
                }
            }
            is SocketTimeoutException -> context.getString(R.string.error_timeout)
            is UnknownHostException -> context.getString(R.string.error_no_internet)
            is IOException -> context.getString(R.string.error_network)
            else -> context.getString(R.string.error_unknown)
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