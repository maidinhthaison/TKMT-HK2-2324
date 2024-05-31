package com.mdts.eieapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class ChatRequestDTO(
    val model: String? = null,
    val messages: List<MessageRequestItemDTO>? = null,
) : Serializable

data class MessageRequestItemDTO(
    val role : String? = null,
    val content: String? = null
) : Serializable