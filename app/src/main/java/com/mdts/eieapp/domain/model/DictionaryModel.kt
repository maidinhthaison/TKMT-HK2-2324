package com.mdts.eieapp.domain.model

import java.io.Serializable


data class DictionaryModel(

    val word: String? = null,
    val phoneticsList: List<PhoneticsModel>? = null,
    val meaningsList: List<MeaningModel>? = null,
    val license: PhoneticsLicenseModel? = null,
    val sourceUrls: String? = null

) : Serializable

data class PhoneticsModel(
    val text: String? = null,
    val audio: String? = null,
    val sourceUrl: String? = null,
    val license: PhoneticsLicenseModel? = null

) : Serializable

data class MeaningModel(
    val partOfSpeech: String? = null,
    val definitions: List<MeaningDefinitionModel>? = null,
    val synonyms: String? = null,
    val antonyms: String? = null

) : Serializable

data class PhoneticsLicenseModel(
    val name: String? = null,
    val url: String? = null

) : Serializable

data class MeaningDefinitionModel(
    val definition: String? = null,
    val synonyms: List<SynonymsModel>? = null,
    val antonyms: List<AntonymsModel>? = null,
    val example: String? = null

) : Serializable

data class SynonymsModel(
    val definition: String? = null,
    val synonyms: String? = null,
    val antonyms: String? = null

) : Serializable

data class AntonymsModel(
    val definition: String? = null,
    val synonyms: String? = null,
    val antonyms: String? = null
) : Serializable

