package com.mdts.eieapp.data.usecase.chat

import com.mdts.eieapp.data.dto.chat.ChatRequestDTO
import com.mdts.eieapp.domain.TaskResult
import com.mdts.eieapp.domain.model.ChatModel
import com.mdts.eieapp.domain.repository.ChatRepository
import com.mdts.eieapp.domain.usecase.chat.SendChatToOpenAIUseCase
import kotlinx.coroutines.flow.Flow

class SendChatToOpenAIUseCaseImpl(private val chatRepository: ChatRepository) :
    SendChatToOpenAIUseCase {
    override fun invoke(chatRequestDTO: ChatRequestDTO): Flow<TaskResult<ChatModel>> {
        return chatRepository.sendChatToOpenAI(chatRequestDTO = chatRequestDTO)
    }
}