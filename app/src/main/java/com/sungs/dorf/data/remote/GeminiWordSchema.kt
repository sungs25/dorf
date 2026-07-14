package com.sungs.dorf.data.remote

import com.google.firebase.ai.type.Schema

    val basicWordSchema: Schema = Schema.obj(
        properties = mapOf(
            "word" to Schema.string(),          // 기본형(관사 제외)
            "meaning" to Schema.string(),       // 핵심 뜻
            "additionalMeanings" to Schema.array(Schema.string()),
            "partOfSpeech" to Schema.enumeration(  // ← enum으로 못박음
                listOf("noun", "verb", "adjective", "adverb", "other")
            ),
            "gender" to Schema.string(),        // der|die|das (명사 아니면 빈 문자열)
            "pluralForm" to Schema.string(),
            "simpleExample" to Schema.string(),
            "simpleExampleKo" to Schema.string(),
        ),
        // 명사 아닐 때 없는 필드들 → 선택적으로
        optionalProperties = listOf("gender", "pluralForm", "additionalMeanings"),
    )



fun basicWordPrompt(userInput: String) = """
독일어 단어 "$userInput"의 정보를 채워라.
- word: 기본형(Grundform). 사용자가 변화형을 넣어도 기본형으로.
- 명사면 gender(der/die/das)와 pluralForm을 채우고, 아니면 비워라.
- meaning: 가장 일반적이고 중립적인 핵심 뜻 하나.
- additionalMeanings: 학습자가 실제로 자주 쓰는 일반적인 뜻만. 비속어·모욕·희귀 용법은 제외. 없으면 빈 배열.
""".trimIndent()