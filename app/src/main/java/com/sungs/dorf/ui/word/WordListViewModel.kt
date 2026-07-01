package com.sungs.dorf.ui.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sungs.dorf.data.local.Word
import com.sungs.dorf.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


data class WordListUiState(
    val words: List<Word> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true
)


@HiltViewModel
class WordListViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val wordsFlow = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) repository.getAllWords()
            else repository.searchWords(query)
        }
    // combine으로 검색어랑 합쳐서 → stateIn으로 hot 변환

    val uiState: StateFlow<WordListUiState> = combine(
        _searchQuery,
        wordsFlow,
    ) { query, words ->
        WordListUiState(words = words, searchQuery = query, isLoading = false)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),   // 화면 잠깐 벗어나도 5초 유지
        initialValue = WordListUiState(),                  // 첫 프레임용 초기값
    )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun deleteWord(word: Word) {
        viewModelScope.launch { repository.deleteWord(word) }
    }
}