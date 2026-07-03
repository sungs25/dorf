package com.sungs.dorf.data.remote

import com.sungs.dorf.domain.model.ConjugationInfo
import com.sungs.dorf.domain.model.WordInfo

fun GlobalWordDto.toWordInfo(): WordInfo = WordInfo(
    // Basic
    word = basic.word,
    meaning = basic.meaning,
    additionalMeanings = basic.additionalMeanings,
    partOfSpeech = basic.partOfSpeech,
    gender = basic.gender,
    pluralForm = basic.pluralForm,
    simpleExample = basic.simpleExample,
    simpleExampleKo = basic.simpleExampleKo,

    // Premium (premium 통째 null이면 각 필드 fallback)
    pronunciation  = premium?.pronunciation,
    isIrregular    = premium?.irregular ?: false,
    isSeparable    = premium?.separable ?: false,
    isReflexive    = premium?.reflexive ?: false,
    conjugation    = premium?.conjugation?.toInfo(),
    caseGovernment = premium?.caseGovernment,
    comparative    = premium?.comparative,
    superlative    = premium?.superlative,
    synonyms       = premium?.synonyms ?: emptyList(),
    antonyms       = premium?.antonyms ?: emptyList(),
    richExamples   = premium?.richExamples ?: emptyList(),
)

private fun ConjugationDto.toInfo() = ConjugationInfo(
    presentThird = praesensEr,
    pastThird = praeteritum,
    perfect = perfekt,
)