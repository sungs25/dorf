package com.sungs.dorf.ui.word

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordScreen(
    onNavigateBack: () -> Unit,                    // 저장 성공/취소 시 뒤로
    viewModel: AddWordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 일회성 이벤트 수집: 저장 성공 시 뒤로
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                AddWordViewModel.AddWordEvent.SavedSuccessfully -> onNavigateBack()
                AddWordViewModel.AddWordEvent.ShowError -> { /* 지금 안 씀 */ }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "단어 추가") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로 가기",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // ⚠️ content는 Column으로 감싸고, paddingValues는 여기 한 번만
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = uiState.word,
                onValueChange = viewModel::onWordChange,
                label = { Text("단어") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.meaning,
                onValueChange = viewModel::onMeaningChange,
                label = { Text("뜻") },
                modifier = Modifier.fillMaxWidth()
            )

            // 품사: 상태는 ViewModel(uiState.partOfSpeech)이 원본
            PartOfSpeechSelector(
                selectedPart = uiState.partOfSpeech,
                onPartSelected = viewModel::onPartOfSpeechChange
            )

            // 명사일 때만 성별/복수형 노출 (ViewModel도 명사 아니면 null로 정리)
            if (uiState.partOfSpeech == "noun") {
                OutlinedTextField(
                    value = uiState.gender ?: "",          // ⚠️ null → "" (아니면 "null" 찍힘)
                    onValueChange = viewModel::onGenderChange,
                    label = { Text("성별 (der/die/das)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.pluralForm,
                    onValueChange = viewModel::onPluralFormChange,
                    label = { Text("복수형") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            // AI 채우기: Phase 2 전까진 비활성화
            Button(
                onClick = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("AI 채우기")
            }

            Button(
                onClick = viewModel::saveWord,
                enabled = uiState.canSave,               // ViewModel 파생값 그대로 사용
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isSaving) CircularProgressIndicator()
                else Text("저장")
            }
        }
    }
}

@Composable
fun PartOfSpeechSelector(
    selectedPart: String,                    // 영어 값 (noun/verb/...)
    onPartSelected: (String) -> Unit
) {
    // 표시용 한글(first) ↔ 저장용 영어(second) 분리
    val partsOfSpeech = listOf(
        "명사" to "noun",
        "동사" to "verb",
        "형용사" to "adjective",
        "부사" to "adverb",
        "기타" to "other",
    )

    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        partsOfSpeech.forEach { (labelKo, value) ->
            FilterChip(
                selected = (selectedPart == value),      // 비교는 영어 값으로
                onClick = { onPartSelected(value) },     // 위로 올리는 것도 영어 값
                label = { Text(text = labelKo) },        // 화면엔 한글
                leadingIcon = if (selectedPart == value) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "선택됨",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else null
            )
        }
    }
}