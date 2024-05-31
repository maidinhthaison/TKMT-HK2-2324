package com.mdts.eieapp.di.chat

import com.mdts.eieapp.data.api.ChatApi
import com.mdts.eieapp.data.repository.ChatRepositoryImpl
import com.mdts.eieapp.data.retrofit.RetrofitManager
import com.mdts.eieapp.data.usecase.SendChatToOpenAIUseCaseImpl
import com.mdts.eieapp.di.DefaultApiQualifier
import com.mdts.eieapp.domain.repository.ChatRepository
import com.mdts.eieapp.domain.usecase.SendChatToOpenAIUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChatModule {
    @Singleton
    @Provides
    fun provideMovieRepository(
        chatApi: ChatApi
    ): ChatRepository {
        return ChatRepositoryImpl(
            chatApi = chatApi
        )
    }

    @Singleton
    @Provides
    fun provideChatApi(@DefaultApiQualifier retrofitManager: RetrofitManager): ChatApi {
        return retrofitManager[ChatApi::class.java]
    }

    @Provides
    fun provideSendChatToOpenAIUseCase(chatRepository: ChatRepository): SendChatToOpenAIUseCase {
        return SendChatToOpenAIUseCaseImpl(chatRepository)
    }
}