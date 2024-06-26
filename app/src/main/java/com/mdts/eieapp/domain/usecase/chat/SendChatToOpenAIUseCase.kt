package com.mdts.eieapp.domain.usecase.chat

import com.mdts.eieapp.data.dto.chat.ChatRequestDTO
import com.mdts.eieapp.domain.TaskResult
import com.mdts.eieapp.domain.model.ChatModel
import kotlinx.coroutines.flow.Flow

interface SendChatToOpenAIUseCase {
    operator fun invoke(chatRequestDTO: ChatRequestDTO): Flow<TaskResult<ChatModel>>
}