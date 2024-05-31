package com.mdts.eieapp.config

enum class BackendEnvironment (
    val baseUrl: String,
    val openAiAPIKey: String
) {

    Dev(
        baseUrl = baseUrl,
        openAiAPIKey = API_KEY
    ),

    Staging(
        baseUrl = baseUrl,
        openAiAPIKey = API_KEY
    ),

    Prod(
        baseUrl = baseUrl,
        openAiAPIKey = API_KEY
    )

}
private const val baseUrl : String = "https://api.openai.com"
private const val API_KEY: String  = ""
const val API_VERSION = "v1"


