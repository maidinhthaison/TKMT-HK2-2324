package com.mdts.eieapp.domain

sealed class AppError {
    //API response error
    class ResponseError(
        val errorCode: String? = null,
        val message: String = "Response Error",
        val errorId: String? = null
    ) : AppError()

    class NetworkError(val httpCode: Int? = null, val message: String = "Network Error") :
        AppError()

    class ServerError(val message: String = "Server internal error") : AppError()

    // For other error
    class GeneralError(val throwable: Throwable) : AppError()

    fun getErrorMessage(): String {
        return when (this) {
            is ResponseError -> message
            is NetworkError -> message
            is ServerError -> message
            is GeneralError -> throwable.message ?: "Unknown error"
        }
    }
}