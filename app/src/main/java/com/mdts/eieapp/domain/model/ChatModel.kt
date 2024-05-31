package com.mdts.eieapp.domain.model

import java.io.Serializable

data class ChatModel(

    val id: String? = null,
    val completion: String? = null,
    val created: Long? = null,
    val model: String? = null,
    val system_fingerprint: String? = null,
    val choices: List<ChoiceItemModel>? = null,
    val usage: UsageItemModel? = null

) : Serializable

data class ChoiceItemModel(
    val index: String? = null,
    val message: ChoiceMessageItemModel? = null,
    val logprobs: String? = null,
    val finish_reason: String? = null
) : Serializable

data class ChoiceMessageItemModel(
    val role: String? = null,
    val content: String? = null
) : Serializable

data class UsageItemModel(
    val prompt_tokens: Int? = null,
    val completion_tokens: Int? = null,
    val total_tokens: Int? = null
) : Serializable
