package com.mdts.eieapp.domain.usecase.dictionary

import com.mdts.eieapp.domain.TaskResult
import com.mdts.eieapp.domain.model.DictionaryModel
import kotlinx.coroutines.flow.Flow

interface LookUpSingleWordUseCase {
    operator fun invoke(word: String): Flow<TaskResult<List<DictionaryModel>>>
}