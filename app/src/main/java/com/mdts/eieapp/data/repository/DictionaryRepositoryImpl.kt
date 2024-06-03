package com.mdts.eieapp.data.repository

import com.mdts.eieapp.data.SafeCallAPI
import com.mdts.eieapp.data.api.DictionaryApi
import com.mdts.eieapp.domain.TaskResult
import com.mdts.eieapp.domain.map
import com.mdts.eieapp.domain.model.DictionaryModel
import com.mdts.eieapp.domain.repository.DictionaryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DictionaryRepositoryImpl (private val dictionaryApi: DictionaryApi,
                                private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO) :
    DictionaryRepository {
    override fun lookUp(word: String): Flow<TaskResult<List<DictionaryModel>>> = flow {
        emit(TaskResult.Loading)
        val result = SafeCallAPI.callApi {
            dictionaryApi.lookUp(word =  word)
        }.map { it -> it.map { it.toDictionaryModel() } }
        emit(result)
    }.flowOn(defaultDispatcher)

}
