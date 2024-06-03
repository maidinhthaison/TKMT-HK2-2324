package com.mdts.eieapp.presentation.bottomsheet

import com.mdts.eieapp.base.BaseUIModel
import com.mdts.eieapp.domain.model.DictionaryModel

data class DictionaryUIModel (
    override val data: List<DictionaryModel>? = null,
    override val isLoading: Boolean = false,
    override val messageId: Int? = null
) : BaseUIModel() {
    override fun copyWith(data: Any?, isLoading: Boolean, messageId: Int?): BaseUIModel {
        return this.copy(
            data = (data ?: this.data) as List<DictionaryModel>?,
            messageId = messageId ?: this.messageId,
            isLoading = isLoading
        )
    }

}
