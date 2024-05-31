package com.mdts.eieapp.presentation.chat

import com.mdts.eieapp.base.BaseUIModel
import com.mdts.eieapp.domain.model.ChatModel

data class ChatUIModel  (
    override val data: ChatModel? = null,
    override val isLoading: Boolean = false,
    override val messageId: Int? = null
) : BaseUIModel() {
    override fun copyWith(data: Any?, isLoading: Boolean, messageId: Int?): BaseUIModel {
        return this.copy(
            data = (data ?: this.data) as ChatModel?,
            messageId = messageId ?: this.messageId,
            isLoading = isLoading
        )
    }
}