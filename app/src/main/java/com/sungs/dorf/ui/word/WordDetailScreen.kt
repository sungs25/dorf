package com.sungs.dorf.ui.word

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sungs.dorf.data.local.Word
import com.sungs.dorf.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * 상세는 별도 UiState 없이 "로딩(null) → 단어" 만 표현.
 * 화면 열릴 때 id로 한 번 조회.
 */
@HiltViewModel
class WordDetailViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {

    private val _word = MutableStateFlow<Word?>(null)
    val word: StateFlow<Word?> = _word.asStateFlow()

    suspend fun load(id: Int) {
        _word.value = repository.getWord(id)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordDetailScreen(
    wordId: Int,
    onNavigateBack: () -> Unit,
    viewModel: WordDetailViewModel = hiltViewModel(),
) {
    val word by viewModel.word.collectAsStateWithLifecycle()

    LaunchedEffect(wordId) {
        viewModel.load(wordId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("단어 상세") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val current = word
        if (current == null) {
            // 아직 조회 전 (로딩)
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // ── 표제어 ──
                Text(
                    text = if (current.gender != null)
                        "${current.gender} ${current.word}"
                    else current.word,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                // 뜻 + 품사
                Text(
                    text = current.meaning,
                    style = MaterialTheme.typography.titleMedium
                )
                DetailRow("품사", current.partOfSpeech)

                // 복수형 (있을 때만)
                if (current.pluralForm != null) {
                    DetailRow("복수형", current.pluralForm)
                }

                // 예문 (있을 때만)
                if (current.simpleExample != null) {
                    HorizontalDivider()
                    Text("예문", style = MaterialTheme.typography.labelLarge)
                    Text(current.simpleExample, style = MaterialTheme.typography.bodyLarge)
                    if (current.simpleExampleKo != null) {
                        Text(
                            current.simpleExampleKo,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // ── 유료(풍부) 정보: 하나라도 있을 때만 섹션 노출 ──
                val hasRich = current.pronunciation != null ||
                        current.conjugation != null ||
                        current.comparative != null ||
                        !current.synonyms.isNullOrEmpty()

                if (hasRich) {
                    HorizontalDivider()

                    if (current.pronunciation != null) DetailRow("발음", current.pronunciation)
                    if (current.comparative != null) DetailRow("비교급", current.comparative)
                    if (current.superlative != null) DetailRow("최상급", current.superlative)
                    if (current.caseGovernment != null) DetailRow("격 지배", current.caseGovernment)

                    if (!current.synonyms.isNullOrEmpty()) {
                        DetailRow("동의어", current.synonyms.joinToString(", "))
                    }
                    if (!current.antonyms.isNullOrEmpty()) {
                        DetailRow("반의어", current.antonyms.joinToString(", "))
                    }

                    // 동사 활용 (객체라 풀어서 표시)
                    val c = current.conjugation
                    if (c != null) {
                        Text("활용", style = MaterialTheme.typography.labelLarge)
                        if (c.praesensEr != null) DetailRow("현재(3인칭)", c.praesensEr)
                        if (c.praeteritum != null) DetailRow("과거", c.praeteritum)
                        if (c.perfekt != null) DetailRow("현재완료", c.perfekt)
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}