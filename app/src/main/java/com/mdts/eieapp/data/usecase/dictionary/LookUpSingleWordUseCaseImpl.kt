package com.mdts.eieapp.data.usecase.dictionary

import com.mdts.eieapp.domain.TaskResult
import com.mdts.eieapp.domain.model.DictionaryModel
import com.mdts.eieapp.domain.repository.DictionaryRepository
import com.mdts.eieapp.domain.usecase.dictionary.LookUpSingleWordUseCase
import kotlinx.coroutines.flow.Flow

class LookUpSingleWordUseCaseImpl (private val dictionaryRepository: DictionaryRepository) :
    LookUpSingleWordUseCase {

    override fun invoke(word: String): Flow<TaskResult<List<DictionaryModel>>> {
        return dictionaryRepository.lookUp(word = word)
    }
}