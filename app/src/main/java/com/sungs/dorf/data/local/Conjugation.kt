package com.sungs.dorf.data.local

import kotlinx.serialization.Serializable

@Serializable
data class Conjugation(
    val praesensEr: String?,   // 현재 3인칭 단수
    val praeteritum: String?,  // 과거
    val perfekt: String?       // 현재완료
)