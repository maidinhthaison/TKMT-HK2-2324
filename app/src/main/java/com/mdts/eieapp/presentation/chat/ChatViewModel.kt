package com.mdts.eieapp.presentation.chat

import androidx.lifecycle.viewModelScope
import com.mdts.eieapp.base.BaseViewModel
import com.mdts.eieapp.data.dto.chat.ChatRequestDTO
import com.mdts.eieapp.domain.usecase.chat.SendChatToOpenAIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendChatToOpenAIUseCase: SendChatToOpenAIUseCase
) : BaseViewModel() {

    private val _uiSendChatModel = MutableStateFlow(ChatUIModel())
    val uiSendChatModel = _uiSendChatModel.asStateFlow()

    fun sendChatToOpenAI(chatRequestDTO: ChatRequestDTO) {
        viewModelScope.launch {
            sendChatToOpenAIUseCase(chatRequestDTO = chatRequestDTO).collectAsState(_uiSendChatModel)
        }
    }


    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()
    private fun setLoading(isLoading: Boolean) {
        _loadingState.value = isLoading
    }
}