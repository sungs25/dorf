package com.sungs.dorf.data.remote

data class GlobalWordDto(
    val basic: BasicDto = BasicDto(),
    val premium: PremiumDto? = null,        // 무료 캐시엔 없음 → nullable
    val metadata: MetadataDto = MetadataDto()
)

// ── 2겹: basic (무료, 8.3) ──
data class BasicDto(
    val word: String = "",
    val meaning: String = "",
    val additionalMeanings: List<String> = emptyList(),
    val partOfSpeech: String = "",
    val gender: String? = null,
    val pluralForm: String? = null,
    val simpleExample: String? = null,
    val simpleExampleKo: String? = null,
)

// ── 2겹: premium (유료, 8.4) ──
data class PremiumDto(
    val pronunciation: String? = null,       // IPA
    val conjugation: ConjugationDto? = null, // ← 3겹으로 또 중첩
    val irregular: Boolean = false,          // ⚠️ is 뗀 이름 (아래 설명)
    val separable: Boolean = false,
    val reflexive: Boolean = false,
    val caseGovernment: String? = null,      // Akk|Dat|Gen|null
    val comparative: String? = null,
    val superlative: String? = null,
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList(),
    val richExamples: List<String> = emptyList(),
)

// ── 3겹: conjugation (8.4의 conjugation 객체) ──
data class ConjugationDto(
    val praesensEr: String? = null,   // "er ..." 3인칭 현재
    val praeteritum: String? = null,
    val perfekt: String? = null,
)

// ── 2겹: metadata ──
data class MetadataDto(
    val createdAt: Long = 0L,
    val reportCount: Int = 0,
)