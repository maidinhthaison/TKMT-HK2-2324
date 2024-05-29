package com.example.movedb.base

import java.io.Serializable

abstract class BaseUIModel : Serializable {
    open val data: Any? = null
    open val isLoading: Boolean = false
    open val messageId: Int? = null
    abstract fun copyWith(
        data: Any? = this.data,
        isLoading: Boolean = this.isLoading,
        messageId: Int? = this.messageId
    ): BaseUIModel
}