package com.mdts.eieapp.config

enum class BackendEnvironment (
    val baseUrl: String,
    val openAiAPIKey: String,
    val dictionaryBaseUrl: String
) {

    Dev(
        baseUrl = baseUrl,
        openAiAPIKey = OPENAI_API_KEY,
        dictionaryBaseUrl = dictionaryBaseUrl
    ),

    Staging(
        baseUrl = baseUrl,
        openAiAPIKey = OPENAI_API_KEY,
        dictionaryBaseUrl = dictionaryBaseUrl
    ),

    Prod(
        baseUrl = baseUrl,
        openAiAPIKey = OPENAI_API_KEY,
        dictionaryBaseUrl = dictionaryBaseUrl
    )

}


private const val baseUrl : String = "https://api.openai.com"
private const val OPENAI_API_KEY: String  = ""
const val API_VERSION = "v1"
////////////
private const val dictionaryBaseUrl : String = "https://api.dictionaryapi.dev"
const val DICT_API_VERSION = "v2"
