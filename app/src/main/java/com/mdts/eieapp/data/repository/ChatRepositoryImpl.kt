package com.mdts.eieapp.data.repository

import com.mdts.eieapp.data.SafeCallAPI
import com.mdts.eieapp.data.api.ChatApi
import com.mdts.eieapp.data.dto.chat.ChatRequestDTO
import com.mdts.eieapp.domain.TaskResult
import com.mdts.eieapp.domain.map
import com.mdts.eieapp.domain.model.ChatModel
import com.mdts.eieapp.domain.repository.ChatRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ChatRepositoryImpl (private val chatApi: ChatApi,
                          private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO) : ChatRepository {
    override fun sendChatToOpenAI(chatRequestDTO: ChatRequestDTO): Flow<TaskResult<ChatModel>> = flow {
        emit(TaskResult.Loading)
        val result = SafeCallAPI.callApi {
            chatApi.sendChatToOpenAI(requestDTO = chatRequestDTO)
        }.map { it.toChatModel() }
        emit(result)
    }.flowOn(defaultDispatcher)

}

