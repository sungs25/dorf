package com.sungs.dorf.ui.word

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sungs.dorf.data.local.Word
import com.sungs.dorf.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class AddWordUiState(
    val word: String = "",
    val meaning: String = "",
    val partOfSpeech: String = "noun",   // 기본값 하나 정해두기
    val gender: String? = null,          // 명사일 때만
    val pluralForm: String = "",
    // ... 수동 입력으로 받을 선택 필드들
    val isSaving: Boolean = false,       // 저장 중 (버튼 중복 클릭 방지)
    val errorMessage: String? = null,    // 검증 실패 메시지
) {
    // 저장 가능 여부를 state에서 파생 — 화면은 이걸로 버튼 enable 판단
    val canSave: Boolean
        get() = word.isNotBlank() && meaning.isNotBlank() && !isSaving
}

@HiltViewModel
class AddWordViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddWordUiState())
    val uiState: StateFlow<AddWordUiState> = _uiState.asStateFlow()

    private val _events = Channel<AddWordEvent>()
    val events = _events.receiveAsFlow()

    sealed interface AddWordEvent {
        data object SavedSuccessfully : AddWordEvent  // 화면이 받으면 뒤로가기
        data object ShowError : AddWordEvent
    }

    fun onWordChange(value: String) {
        _uiState.update { it.copy(word = value, errorMessage = null) }
    }
    fun onMeaningChange(value: String) {
        _uiState.update { it.copy(meaning = value, errorMessage = null) }
    }
    fun onPartOfSpeechChange(value: String) {
        _uiState.update {
            if (value != "noun") it.copy(partOfSpeech = value, gender = null, pluralForm = "")
            else it.copy(partOfSpeech = value)
        }
    }
    fun onGenderChange(value: String) {
        _uiState.update { it.copy(gender = value) }
    }
    fun onPluralFormChange(value: String) {
        _uiState.update { it.copy(pluralForm = value) }
    }

    fun saveWord() {
        val current = _uiState.value

        // (1) 방어적 검증 — canSave로 버튼은 막았지만 한 번 더
        if (!current.canSave) return

        // (2) 저장 시작 표시
        _uiState.update { it.copy(isSaving = true, errorMessage = null) }

        // (3) 코루틴으로 저장
        viewModelScope.launch {
            try {
                val newWord = Word.createManual(
                    word = current.word.trim(),
                    meaning = current.meaning.trim(),
                    partOfSpeech = current.partOfSpeech,
                    gender = current.gender,
                    pluralForm = current.pluralForm.ifBlank { null },
                )
                repository.addWord(newWord)   // 여기서 정규화 키 채워서 insert

                // (4) 성공 → 일회성 이벤트
                _events.send(AddWordEvent.SavedSuccessfully)

            } catch (e: Exception) {
                // (5) 실패 → state에 에러 + isSaving 되돌리기
                _uiState.update {
                    it.copy(isSaving = false, errorMessage = "저장에 실패했어요")
                }
            }
        }
    }
}

