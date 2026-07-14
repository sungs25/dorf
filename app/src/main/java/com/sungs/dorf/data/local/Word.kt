package com.sungs.dorf.data.local

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "words",
    indices = [Index("normalizedWord")]  // 검색·중복조회 빠르게
)
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // ── 기본 정보 (무료) ──
    val word: String,                       // 관사 제외 기본형
    val normalizedWord: String,             // 캐시 키
    val meaning: String,                    // 핵심 한국어 뜻
    val additionalMeanings: List<String>?,  // 부가 뜻 → 컨버터 필요
    val partOfSpeech: String,               // noun/verb/adjective/adverb/other
    val gender: String?,                    // der/die/das, 명사 아니면 null
    val pluralForm: String?,                // 복수형, 없으면 null
    val simpleExample: String?,             // 독일어 예문 1개
    val simpleExampleKo: String?,           // 그 예문 한국어 번역

    // ── 풍부한 정보 (유료) ──
    val pronunciation: String?,             // IPA 발음기호
    val conjugation: Conjugation?,          // 동사 3변화 → 컨버터 필요 (아래 설명)
    val isIrregular: Boolean?,              // 불규칙 동사 여부
    val isSeparable: Boolean?,              // 분리동사 여부
    val isReflexive: Boolean?,              // 재귀동사 여부
    val caseGovernment: String?,            // 격 지배: Akk/Dat/Gen/null
    val comparative: String?,               // 비교급
    val superlative: String?,               // 최상급
    val synonyms: List<String>?,            // 동의어 → 컨버터 필요
    val antonyms: List<String>?,            // 반의어 → 컨버터 필요
    val richExamples: List<String>?,        // 예문 2개 → 컨버터 필요

    // ── SRS (간격 반복) ──
    val nextReviewDate: Long,               // 다음 복습 시각 (epoch millis)
    val intervalDays: Int,                  // 현재 복습 간격
    val easeFactor: Float,                  // SM-2 난이도 계수 (시작 2.5)
    val isLearned: Boolean,                 // "외움" 처리 여부

    // ── 메타 ──
    val createdAt: Long,
    val updatedAt: Long
) {
    companion object {
        fun createManual(
            word: String,
            meaning: String,
            partOfSpeech: String,
            gender: String?,
            pluralForm: String?,
        ): Word {
            val now = System.currentTimeMillis()
            return Word(
                word = word,
                normalizedWord = "",  // Repository가 채움 (책임 분리)
                meaning = meaning,
                partOfSpeech = partOfSpeech,
                gender = gender,
                pluralForm = pluralForm,
                // SRS 기본값
                nextReviewDate = now,   // 지금부터 복습 가능
                intervalDays = 0,
                easeFactor = 2.5f,
                isLearned = false,
                // 메타
                createdAt = now,
                updatedAt = now,
                // 나머지 유료 필드는 전부 null (수동 입력이니까)
                additionalMeanings = null,
                simpleExample = null,
                simpleExampleKo = null,
                pronunciation = null,

                conjugation = null,
                isIrregular = null,
                isSeparable = null,
                isReflexive = null,
                caseGovernment = null,
                comparative = null,
                superlative = null,
                synonyms = null,
                antonyms = null,
                richExamples = null
            )
        }
    }
}