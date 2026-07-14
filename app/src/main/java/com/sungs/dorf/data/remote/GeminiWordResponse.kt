package com.sungs.dorf.data.remote

import com.sungs.dorf.domain.model.WordInfo
import kotlinx.serialization.Serializable

@Serializable
data class GeminiWordResponse(
    val word: String,
    val meaning: String,
    val additionalMeanings: List<String> = emptyList(),
    val partOfSpeech: String,
    val gender: String? = null,
    val pluralForm: String? = null,
    val simpleExample: String? = null,
    val simpleExampleKo: String? = null,
)

fun GeminiWordResponse.toWordInfo() = WordInfo(
    word = word, meaning = meaning,
    additionalMeanings = additionalMeanings,
    partOfSpeech = partOfSpeech,
    gender = gender, pluralForm = pluralForm,
    simpleExample = simpleExample, simpleExampleKo = simpleExampleKo,
    // premium 필드 (무료 응답엔 없음)
    pronunciation = null, isIrregular = false, isSeparable = false,
    isReflexive = false, conjugation = null, caseGovernment = null,
    comparative = null, superlative = null,
    synonyms = emptyList(), antonyms = emptyList(), richExamples = emptyList(),
)

fun GeminiWordResponse.toGlobalWordDto(): GlobalWordDto = GlobalWordDto(
    basic = BasicDto(
        word = word,
        meaning = meaning,
        additionalMeanings = additionalMeanings,
        partOfSpeech = partOfSpeech,
        gender = gender,
        pluralForm = pluralForm,
        simpleExample = simpleExample,
        simpleExampleKo = simpleExampleKo,
    ),
    premium = null,                    // 무료 응답이라 아직 없음
    metadata = MetadataDto(
        createdAt = System.currentTimeMillis(),
        reportCount = 0,
    ),
)