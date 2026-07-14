package com.sungs.dorf

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.google.firebase.ai.GenerativeModel
import com.sungs.dorf.data.remote.FirestoreWordDataSource
import com.sungs.dorf.data.remote.GeminiWordDataSource
import com.sungs.dorf.data.remote.toWordInfo
import com.sungs.dorf.data.repository.WordInfoRepository
import com.sungs.dorf.ui.navigation.DorfNavHost
import com.sungs.dorf.ui.theme.DorfTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var dataSource: FirestoreWordDataSource
    @Inject
    lateinit var geminiDataSource: GeminiWordDataSource
    @Inject lateinit var generativeModel: GenerativeModel
    @Inject lateinit var wordInfoRepo: WordInfoRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            DorfTheme {          // 네 테마 이름에 맞게
                DorfNavHost()

            }
        }
    }
}

