package com.sungs.dorf.data.repository

import com.sungs.dorf.data.remote.FirestoreWordDataSource
import com.sungs.dorf.data.remote.GeminiWordDataSource
import com.sungs.dorf.data.remote.toGlobalWordDto
import com.sungs.dorf.data.remote.toWordInfo
import com.sungs.dorf.domain.model.WordInfo
import com.sungs.dorf.util.WordNormalizer
import javax.inject.Inject

class WordInfoRepository @Inject constructor(
    private val firestoreSource: FirestoreWordDataSource,
    private val geminiSource: GeminiWordDataSource,
) {
    // 핵심: 입력 → 캐시 조회 → 히트/미스 → (미스면) AI → 재키잉 → 저장 → WordInfo
    suspend fun fetchWordInfo(userInput: String): WordInfo {
        val cacheKey = WordNormalizer.normalizeForCache(userInput)
        val dto = firestoreSource.getGlobalWord(cacheKey)

        return if (dto != null) {
            dto.toWordInfo()                                    // 히트: 비용 0
        } else {
            val response = geminiSource.fetchBasic(userInput)   // 미스: AI
            val baseKey = WordNormalizer.normalizeForCache(response.word)  // 재키잉
            firestoreSource.saveGlobalWord(baseKey, response.toGlobalWordDto())  // 저장
            response.toWordInfo()
        }
    }
}