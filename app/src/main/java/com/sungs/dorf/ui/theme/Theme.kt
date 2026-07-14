package com.sungs.dorf.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DorfLightColors = lightColorScheme(
    primary            = InkGreen,
    onPrimary          = Color.White,
    primaryContainer   = GreenTintBg,
    onPrimaryContainer = InkGreenDark,

    background          = PaperBg,
    onBackground        = InkText,

    surface             = PaperSurface,
    onSurface           = InkText,
    onSurfaceVariant    = InkTextMuted,   // DetailRow 라벨·리스트 뜻·빈상태 문구

    // 칩/뱃지·보조 강조에 그린 톤 연결
    secondaryContainer   = GreenTintBg,
    onSecondaryContainer = InkGreenDark,

    outline             = PaperBorder,
    outlineVariant      = PaperBorder,

    error   = ErrorRed,
    onError = Color.White,
)

@Composable
fun DorfTheme(
    content: @Composable () -> Unit
) {
    // 라이트 전용. dynamicColor·다크 분기 제거 —
    // 나중에 다크 추가하면 여기서 스킴만 분기하면 됨.
    MaterialTheme(
        colorScheme = DorfLightColors,
        typography = Typography,
        content = content
    )
}