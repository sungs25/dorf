package com.sungs.dorf.data.local

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    // 1. 전체 조회
    @Query("SELECT * FROM words ORDER BY createdAt DESC")
    fun getAllWords(): Flow<List<Word>>

    // 2. 검색 (단어 또는 뜻)
    // '%' || :searchQuery || '%' 구문은 앞뒤로 어떤 글자가 오든 searchQuery가 포함된 것을 찾으라는 뜻입니다.
    @Query("""
        SELECT * FROM words 
        WHERE word LIKE '%' || :searchQuery || '%' 
           OR meaning LIKE '%' || :searchQuery || '%' 
        ORDER BY createdAt DESC
    """)
    fun searchWords(searchQuery: String): Flow<List<Word>>

    // 3. ID로 단건 조회
    @Query("SELECT * FROM words WHERE id = :id LIMIT 1")
    suspend fun getWordById(id: Int): Word?

    // 4. 정규화 키로 단건 조회
    @Query("SELECT * FROM words WHERE normalizedWord = :normalizedWord LIMIT 1")
    suspend fun getWordByNormalized(normalizedWord: String): Word?

    // 5. 단어 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: Word)

    // 6. 단어 수정
    @Update
    suspend fun updateWord(word: Word)

    // 7. 단어 삭제
    @Delete
    suspend fun deleteWord(word: Word)
}