package com.mdts.eieapp.data.dto

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class APIResponseErrorDto(
    @SerializedName("id")
    val errorId: String?,
    @SerializedName("code")
    val errorCode: String?,
    @SerializedName("message")
    val errorMessage: String?
) : Serializable {
    companion object {
        fun fromJson(json: String): APIResponseErrorDto? {
            try {
                return Gson().fromJson(json, APIResponseErrorDto::class.java)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        }
    }

}