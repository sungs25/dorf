package com.sungs.dorf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sungs.dorf.data.remote.FirestoreWordDataSource
import com.sungs.dorf.ui.navigation.DorfNavHost
import com.sungs.dorf.ui.theme.DorfTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var dataSource: FirestoreWordDataSource
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            DorfTheme {          // 네 테마 이름에 맞게
                DorfNavHost()

            }
        }
    }
}

