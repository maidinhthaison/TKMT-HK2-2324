package com.mdts.eieapp.data.dto.dictionary

import com.google.gson.annotations.SerializedName
import com.mdts.eieapp.domain.model.AntonymsModel
import com.mdts.eieapp.domain.model.DictionaryModel
import com.mdts.eieapp.domain.model.MeaningDefinitionModel
import com.mdts.eieapp.domain.model.MeaningModel
import com.mdts.eieapp.domain.model.PhoneticsLicenseModel
import com.mdts.eieapp.domain.model.PhoneticsModel
import com.mdts.eieapp.domain.model.SynonymsModel
import java.io.Serializable

data class DictionaryResponseDTO(
    @SerializedName("word") val word: String?,
    @SerializedName("phonetics") val phoneticsList: List<PhoneticsResponseDTO>?,
    @SerializedName("meanings") val meaningsList: List<MeaningResponseDTO>?,
    @SerializedName("license") val license: PhoneticsLicenseResponseDTO?,
    @SerializedName("sourceUrls") val sourceUrls: String?

): Serializable {
    fun toDictionaryModel(): DictionaryModel {
        return DictionaryModel(
            word = word,
            phoneticsList = phoneticsList?.map { it.toPhoneticsModel() },
            meaningsList = meaningsList?.map { it.toMeaningModel() },
            license = license?.toPhoneticsLicenseModel(),
            sourceUrls = sourceUrls
        )

    }
}

data class PhoneticsResponseDTO(
    @SerializedName("text") val text: String?,
    @SerializedName("audio") val audio: String?,
    @SerializedName("sourceUrl") val sourceUrl: String?,
    @SerializedName("license") val phoneticsLicenseResponseDTO: PhoneticsLicenseResponseDTO?
) : Serializable{
    fun toPhoneticsModel(): PhoneticsModel {
        return PhoneticsModel(
            text = text,
            audio = audio,
            sourceUrl = sourceUrl,
            license = phoneticsLicenseResponseDTO?.toPhoneticsLicenseModel()
        )

    }
}

data class PhoneticsLicenseResponseDTO(
    @SerializedName("name") val name: String?,
    @SerializedName("url") val url: String?
) : Serializable{
    fun toPhoneticsLicenseModel(): PhoneticsLicenseModel {
        return PhoneticsLicenseModel(
            name = name,
            url = url
        )

    }
}


data class MeaningResponseDTO(
    @SerializedName("partOfSpeech") val partOfSpeech: String?,
    @SerializedName("definitions") val definitions: List<MeaningDefinitionResponseDTO>?,
    @SerializedName("synonyms") val synonyms: String?,
    @SerializedName("antonyms") val antonyms: String?
) : Serializable{
    fun toMeaningModel(): MeaningModel {
        return MeaningModel(
            partOfSpeech = partOfSpeech,
            definitions = definitions?.map { it.toMeaningDefinitionModel() },
            synonyms = synonyms,
            antonyms = antonyms
        )

    }
}

data class MeaningDefinitionResponseDTO(
    @SerializedName("definition") val definition: String?,
    @SerializedName("synonyms") val synonyms: List<SynonymsResponseDTO>?,
    @SerializedName("antonyms") val antonyms: List<AntonymsResponseDTO>?,
    @SerializedName("example") val example: String?
) : Serializable{
    fun toMeaningDefinitionModel(): MeaningDefinitionModel {
        return MeaningDefinitionModel(
            definition = definition,
            synonyms = synonyms?.map { it.toSynonymsModel() },
            antonyms = antonyms?.map { it.toAntonymsModel() },
            example = example
        )

    }
}

data class SynonymsResponseDTO(
    @SerializedName("definition") val definition: String?,
    @SerializedName("synonyms") val synonyms: String?,
    @SerializedName("antonyms") val antonyms: String?
) : Serializable{
    fun toSynonymsModel(): SynonymsModel {
        return SynonymsModel(
            definition = definition,
            synonyms = synonyms,
            antonyms = antonyms
        )

    }
}

data class AntonymsResponseDTO(
    @SerializedName("definition") val definition: String?,
    @SerializedName("synonyms") val synonyms: String?,
    @SerializedName("antonyms") val antonyms: String?,

) : Serializable{
    fun toAntonymsModel(): AntonymsModel {
        return AntonymsModel(
            definition = definition,
            synonyms = synonyms,
            antonyms = antonyms
        )

    }
}
