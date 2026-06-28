package com.sungs.dorf

import androidx.room3.ColumnTypeConverter
import com.sungs.dorf.data.local.Conjugation
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {

    @ColumnTypeConverter
    fun fromStringList(value: List<String>?): String? {
        if (value == null) return null
        return Json.encodeToString(value)
    }

    @ColumnTypeConverter
    fun toStringList(value: String?): List<String>? {
        if (value == null) return null
        return Json.decodeFromString<List<String>>(value)
    }

    @ColumnTypeConverter
    fun fromConjugation(value: Conjugation?): String? {
        if (value == null) return null
        return Json.encodeToString(value)
    }  // 객체 → JSON 문자열

    @ColumnTypeConverter
    fun toConjugation(value: String?): Conjugation? {
        if (value == null) return null
        return Json.decodeFromString<Conjugation>(value)
    }  // JSON 문자열 → 객체

}