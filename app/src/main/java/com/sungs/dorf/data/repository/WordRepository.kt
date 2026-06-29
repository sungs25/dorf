package com.sungs.dorf.data.repository

import com.sungs.dorf.data.local.Word
import com.sungs.dorf.data.local.WordDao
import com.sungs.dorf.util.WordNormalizer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WordRepository @Inject constructor(
    private val wordDao: WordDao
) {
    // 조회는 DAO의 Flow를 그대로 흘려보냄 (가공 X)
    fun getAllWords(): Flow<List<Word>> = wordDao.getAllWords()
    fun searchWords(query: String): Flow<List<Word>> = wordDao.searchWords(query)

    suspend fun getWord(id: Int): Word? = wordDao.getWordById(id)

    suspend fun addWord(word: Word) {
        val normalized = WordNormalizer.normalizeForCache(word.word)
        wordDao.insertWord(word.copy(normalizedWord = normalized))
    }

    suspend fun updateWord(word: Word) = wordDao.updateWord(word)
    suspend fun deleteWord(word: Word) = wordDao.deleteWord(word)
}