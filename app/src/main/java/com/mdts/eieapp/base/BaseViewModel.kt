package com.mdts.eieapp.base

import androidx.lifecycle.ViewModel
import com.mdts.eieapp.R
import com.mdts.eieapp.domain.AppError
import com.mdts.eieapp.domain.TaskResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

abstract class BaseViewModel : ViewModel() {

    private suspend fun <T : BaseUIModel> emitLoading(
        isLoading: Boolean,
        state: MutableStateFlow<T>
    ) {
        val value = state.value.copyWith(
            isLoading = isLoading,
            messageId = null
        )
        state.emit(value as T)

    }

    private suspend fun <T : BaseUIModel> emitError(
        errorMessage: Int,
        state: MutableStateFlow<T>
    ) {
        val value = state.value.copyWith(
            isLoading = false,
            messageId = errorMessage
        )
        state.emit(value as T)
    }

    private suspend fun <T, T0 : BaseUIModel> emitData(data: T, state: MutableStateFlow<T0>) {
        val value = state.value.copyWith(
            data = data,
            isLoading = false,
            messageId = null
        )
        state.emit(value as T0)
    }

    suspend fun <T, T0 : BaseUIModel> Flow<TaskResult<T>>.collectAsState(
        state: MutableStateFlow<T0>
    ) {
        this.onStart {
            emitLoading(true, state)
        }.catch {
            emitError(toMessageId(it.localizedMessage ?: ""), state)
        }.collect {
            when (it) {
                is TaskResult.Success -> {
                    emitData(it.data, state)
                }

                is TaskResult.Failure -> {
                    emitError(toMessageId(it.error.toString()), state)
                }

                is TaskResult.Loading -> {
                    emitLoading(true, state)
                }

                is TaskResult.LoadingWithProgress -> {
                    emitLoading(true, state)
                }
            }
        }
    }

    suspend fun <T> Flow<TaskResult<T>>.collectAsState(
        state: MutableSharedFlow<TaskResult<T>>
    ) {
        this.onStart {
            state.emit(TaskResult.Loading)
        }.catch {
            state.emit(TaskResult.Failure(AppError.GeneralError(it)))
        }.collect {
            when (it) {
                is TaskResult.Success -> {
                    state.emit(TaskResult.Success(it.data))
                }

                is TaskResult.Failure -> {
                    state.emit(TaskResult.Failure(it.error))
                }

                is TaskResult.Loading -> {
                    state.emit(TaskResult.Loading)
                }

                is TaskResult.LoadingWithProgress -> {
                    state.emit(TaskResult.LoadingWithProgress(it.progress))
                }
            }
        }
    }

    private fun toMessageId(message: String): Int {
        return R.string.message_api_error
    }
}