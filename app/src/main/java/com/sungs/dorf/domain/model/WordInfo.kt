package com.sungs.dorf.domain.model

data class WordInfo(
    // ── Basic (무료, 8.3) ──
    val word: String,
    val meaning: String,
    val additionalMeanings: List<String>,
    val partOfSpeech: String,
    val gender: String?,
    val pluralForm: String?,
    val simpleExample: String?,
    val simpleExampleKo: String?,

    // ── Premium (유료, 8.4) ──
    val pronunciation: String?,          // IPA
    val isIrregular: Boolean,
    val isSeparable: Boolean,
    val isReflexive: Boolean,
    val conjugation: ConjugationInfo?,
    val caseGovernment: String?,         // Akk|Dat|Gen|null
    val comparative: String?,
    val superlative: String?,
    val synonyms: List<String>,
    val antonyms: List<String>,
    val richExamples: List<String>,
)

// premium.conjugation 대응. DTO의 ConjugationDto와 1:1, 이름만 도메인 관례로.
data class ConjugationInfo(
    val presentThird: String?,   // er geht — 3인칭 현재
    val pastThird: String?,      // er ging — 3인칭 과거(Präteritum)
    val perfect: String?,        // ist gegangen — 현재완료
)