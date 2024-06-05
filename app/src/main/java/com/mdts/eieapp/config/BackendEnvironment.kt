package com.mdts.eieapp.config

enum class BackendEnvironment (
    val openAiBaseUrl: String,
    val openAiAPIKey: String,
    val dictionaryBaseUrl: String
) {

    Dev(
        openAiBaseUrl = openAiBaseUrl,
        openAiAPIKey = OPENAI_API_KEY,
        dictionaryBaseUrl = dictionaryBaseUrl
    ),

    Staging(
        openAiBaseUrl = openAiBaseUrl,
        openAiAPIKey = OPENAI_API_KEY,
        dictionaryBaseUrl = dictionaryBaseUrl
    ),

    Prod(
        openAiBaseUrl = openAiBaseUrl,
        openAiAPIKey = OPENAI_API_KEY,
        dictionaryBaseUrl = dictionaryBaseUrl
    )

}


private const val openAiBaseUrl : String = "https://api.openai.com"
private const val OPENAI_API_KEY: String  = ""
const val API_VERSION = "v1"
////////////
private const val dictionaryBaseUrl : String = "https://api.dictionaryapi.dev"
const val DICT_API_VERSION = "v2"
