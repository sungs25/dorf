package com.sungs.dorf.data.remote

import com.google.firebase.ai.GenerativeModel
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GeminiWordDataSource @Inject constructor(
    private val generativeModel: GenerativeModel   // 스키마 박힌 모델 주입
) {
    suspend fun fetchBasic(userInput: String): GeminiWordResponse {
        val response = generativeModel.generateContent(basicWordPrompt(userInput))
        val json = response.text ?: error("빈 응답")
        return Json.decodeFromString<GeminiWordResponse>(json)  // 스키마 강제라 순수 JSON
    }
}