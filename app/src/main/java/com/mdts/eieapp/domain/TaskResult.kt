package com.mdts.eieapp.domain

sealed class TaskResult<out T>(val loading: Boolean) {
    object Loading : TaskResult<Nothing>(loading = true)
    data class Success<out T>(val data: T) : TaskResult<T>(loading = false)
    data class Failure(val error: AppError) : TaskResult<Nothing>(loading = false)
    data class LoadingWithProgress(val progress: Long) : TaskResult<Nothing>(loading = true)

    fun isSuccess() = this is Success

    fun isError() = this is Failure

    fun value(): T? {
        return when (this) {
            is Success -> this.data
            else -> null
        }
    }

    fun error(): AppError? {
        return when (this) {
            is Failure -> this.error
            else -> null
        }
    }
}

inline fun <T, P : Any> TaskResult<T>.map(block: (T) -> P): TaskResult<P> {
    return when (this) {
        is TaskResult.Success -> TaskResult.Success(block(this.data))
        is TaskResult.Failure -> this
        is TaskResult.LoadingWithProgress -> this
        TaskResult.Loading -> TaskResult.Loading
    }
}