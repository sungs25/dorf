package com.sungs.dorf.data.local

import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

interface WordDao {
    @Query("SELECT * FROM words")
    fun getAll(): Flow<List<Word>>


}