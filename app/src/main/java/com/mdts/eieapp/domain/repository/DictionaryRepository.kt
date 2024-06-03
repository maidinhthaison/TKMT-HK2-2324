package com.mdts.eieapp.domain.repository

import com.mdts.eieapp.domain.TaskResult
import com.mdts.eieapp.domain.model.DictionaryModel
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun lookUp(word: String): Flow<TaskResult<List<DictionaryModel>>>
}