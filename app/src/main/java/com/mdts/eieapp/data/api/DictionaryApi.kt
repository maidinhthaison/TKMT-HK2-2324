package com.mdts.eieapp.data.api

import com.mdts.eieapp.config.DICT_API_VERSION
import com.mdts.eieapp.data.dto.dictionary.DictionaryResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET("/api/$DICT_API_VERSION/entries/en/{word}")
    suspend fun lookUp(
        @Path("word") word: String,
    ): Response<List<DictionaryResponseDTO>>
}