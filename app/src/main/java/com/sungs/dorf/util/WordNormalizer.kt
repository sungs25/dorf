package com.sungs.dorf.util

object WordNormalizer {

    // 정규식 객체를 매번 생성하지 않도록 클래스 레벨로 분리 (성능 최적화)
    private val articleRegex = Regex("^(der|die|das|des|dem|den|ein|eine|eines|einer|einem|einen)\\s+")

    fun normalizeForCache(input: String): String {
        return input
            .lowercase()        // 1. 소문자 변환
            .trim()             // 2. 앞뒤 공백 제거
            .replace(articleRegex, "")  // 3. 선행 관사 모두 제거
            .replace("ä", "ae") // 4. 움라우트 및 ß 치환
            .replace("ö", "oe")
            .replace("ü", "ue")
            .replace("ß", "ss")
    }
}