package com.mdts.eieapp.config

enum class BackendEnvironment (
    val baseUrl: String,
    val openAiAPIKey: String
) {

    Dev(
        baseUrl = baseUrl,
        openAiAPIKey = OPENAI_API_KEY
    ),

    Staging(
        baseUrl = baseUrl,
        openAiAPIKey = OPENAI_API_KEY
    ),

    Prod(
        baseUrl = baseUrl,
        openAiAPIKey = OPENAI_API_KEY
    )

}
private const val baseUrl : String = "https://api.openai.com"
//private const val OPENAI_API_KEY: String  = "sk-proj-Sks7W4353nrnpJlclhUsT3BlbkFJ1uWCAfSMQGEtI79ZBNfE"
private const val OPENAI_API_KEY: String  = ""
const val API_VERSION = "v1"


