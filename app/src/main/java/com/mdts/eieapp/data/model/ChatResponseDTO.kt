package com.mdts.eieapp.data.model

import com.google.gson.annotations.SerializedName
import com.mdts.eieapp.domain.model.ChatModel
import com.mdts.eieapp.domain.model.ChoiceItemModel
import com.mdts.eieapp.domain.model.ChoiceMessageItemModel
import com.mdts.eieapp.domain.model.UsageItemModel
import java.io.Serializable

data class ChatResponseDTO (
    @SerializedName("id") val id: String?,
    @SerializedName("object") val completion: String?,
    @SerializedName("created") val created: Long?,
    @SerializedName("model") val model: String?,
    @SerializedName("system_fingerprint") val system_fingerprint: String?,
    @SerializedName("choices") val choices: List<ChoicesResponseDTO>?,
    @SerializedName("usage") val usage: UsageResponseDTO?
): Serializable {
    fun toChatModel(): ChatModel {
        return ChatModel(
            id = id,
            completion = completion,
            created = created,
            model = model,
            system_fingerprint = system_fingerprint,
            choices = choices?.map { item -> item.toChoiceItemModel() },
            usage = usage?.toUsageItemModel()
        )

    }
}
data class ChoicesResponseDTO (
    @SerializedName("index") val index: String?,
    @SerializedName("message") val message: ChoicesMessageResponseDTO?,
    @SerializedName("logprobs") val logprobs: String?,
    @SerializedName("finish_reason") val finish_reason: String?
): Serializable{
    fun toChoiceItemModel(): ChoiceItemModel {
        return ChoiceItemModel(
            index = index,
            message =  message?.toChoiceMessageItemModel(),
            logprobs = logprobs,
            finish_reason = finish_reason
        )

    }
}
data class ChoicesMessageResponseDTO (
    @SerializedName("role") val role: String?,
    @SerializedName("content") val content: String?
): Serializable{
    fun toChoiceMessageItemModel(): ChoiceMessageItemModel {
        return ChoiceMessageItemModel(
            role = role,
            content = content
        )

    }
}

data class UsageResponseDTO (
    @SerializedName("prompt_tokens") val prompt_tokens: Int?,
    @SerializedName("completion_tokens") val completion_tokens: Int?,
    @SerializedName("total_tokens") val total_tokens: Int?
): Serializable{
    fun toUsageItemModel(): UsageItemModel {
        return UsageItemModel(
            prompt_tokens = prompt_tokens,
            completion_tokens = completion_tokens,
            total_tokens = total_tokens
        )

    }
}

