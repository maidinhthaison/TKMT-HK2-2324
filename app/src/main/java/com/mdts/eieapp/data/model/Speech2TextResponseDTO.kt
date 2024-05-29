package com.mdts.eieapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Speech2TextResponseDTO(@SerializedName("page") val page: Int?
): Serializable {

}
