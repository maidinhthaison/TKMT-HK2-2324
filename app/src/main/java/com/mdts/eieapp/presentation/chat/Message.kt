package com.mdts.eieapp.presentation.chat

import java.io.Serializable

data class Message (val role: String, val content: String) : Serializable {
}