package com.mdts.eieapp.data.api

import com.mdts.eieapp.config.API_VERSION
import com.mdts.eieapp.data.model.ChatRequestDTO
import com.mdts.eieapp.data.model.ChatResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {
    @POST("/$API_VERSION/chat/completions ")
    suspend fun sendChatToOpenAI(
        @Body requestDTO: ChatRequestDTO
    ): Response<ChatResponseDTO>
}