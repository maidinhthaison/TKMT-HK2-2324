package com.mdts.eieapp.di.dictionary

import com.mdts.eieapp.data.api.DictionaryApi
import com.mdts.eieapp.data.repository.DictionaryRepositoryImpl
import com.mdts.eieapp.data.retrofit.RetrofitManager
import com.mdts.eieapp.data.usecase.dictionary.LookUpSingleWordUseCaseImpl
import com.mdts.eieapp.di.DictionaryApiQualifier
import com.mdts.eieapp.domain.repository.DictionaryRepository
import com.mdts.eieapp.domain.usecase.dictionary.LookUpSingleWordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DictionaryModule {
    @Singleton
    @Provides
    fun provideDictionaryRepository(
        dictionaryApi: DictionaryApi
    ): DictionaryRepository {
        return DictionaryRepositoryImpl(
            dictionaryApi = dictionaryApi
        )
    }

    @Singleton
    @Provides
    fun provideDictionaryApi(@DictionaryApiQualifier retrofitManager: RetrofitManager): DictionaryApi {
        return retrofitManager[DictionaryApi::class.java]
    }

    @Provides
    fun provideLookUpSingleWordUseCase(dictionaryRepository: DictionaryRepository): LookUpSingleWordUseCase {
        return LookUpSingleWordUseCaseImpl(dictionaryRepository)
    }
}