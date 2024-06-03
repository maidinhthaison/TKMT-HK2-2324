package com.mdts.eieapp.presentation.bottomsheet

import androidx.lifecycle.viewModelScope
import com.mdts.eieapp.base.BaseViewModel
import com.mdts.eieapp.domain.TaskResult
import com.mdts.eieapp.domain.model.DictionaryModel
import com.mdts.eieapp.domain.usecase.dictionary.LookUpSingleWordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val lookUpSingleWordUseCase: LookUpSingleWordUseCase
) : BaseViewModel() {

    /*private val _uiLookUpModel = MutableStateFlow(DictionaryUIModel())
    val uiLookUpModel = _uiLookUpModel.asStateFlow()

    fun lookUp(word: String) {
        viewModelScope.launch {
            lookUpSingleWordUseCase(word = word).collectAsState(_uiLookUpModel)
        }
    }*/


    private val _uiLookUpModel = MutableSharedFlow<TaskResult<List<DictionaryModel>>>()
    val uiLookUpModel = _uiLookUpModel.asSharedFlow()

    fun lookUp(word: String) {

        viewModelScope.launch {
            lookUpSingleWordUseCase(word = word)
                .onStart { setLoading(true) }
                .collect {
                    setLoading(it is TaskResult.Loading)
                    _uiLookUpModel.emit(it)
                }
        }
    }

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private fun setLoading(isLoading: Boolean) {
        _loadingState.value = isLoading
    }
}