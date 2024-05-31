package com.mdts.eieapp.domain.repository

import com.mdts.eieapp.data.model.ChatRequestDTO
import com.mdts.eieapp.domain.TaskResult
import com.mdts.eieapp.domain.model.ChatModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun sendChatToOpenAI(chatRequestDTO: ChatRequestDTO): Flow<TaskResult<ChatModel>>
}