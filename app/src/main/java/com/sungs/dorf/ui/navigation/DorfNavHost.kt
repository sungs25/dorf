package com.sungs.dorf.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.sungs.dorf.ui.word.AddWordScreen
import com.sungs.dorf.ui.word.WordDetailScreen
import com.sungs.dorf.ui.word.WordListScreen

// 화면 경로를 한 곳에 모아서 오타 방지
object Routes {
    const val WORD_LIST = "word_list"
    const val ADD_WORD = "add_word"
    const val WORD_DETAIL = "word_detail"       // 실제 경로는 "word_detail/{wordId}"
    const val ARG_WORD_ID = "wordId"
}

@Composable
fun DorfNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.WORD_LIST
    ) {
        // 리스트 화면
        composable(Routes.WORD_LIST) {
            WordListScreen(
                onNavigateToAdd = {
                    navController.navigate(Routes.ADD_WORD)
                },
                onNavigateToDetail = { wordId ->
                    navController.navigate("${Routes.WORD_DETAIL}/$wordId")
                }
            )
        }

        // 단어 추가 화면
        composable(Routes.ADD_WORD) {
            AddWordScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 상세 화면 — 경로에 {wordId} 인자를 담음
        composable(
            route = "${Routes.WORD_DETAIL}/{${Routes.ARG_WORD_ID}}",
            arguments = listOf(
                navArgument(Routes.ARG_WORD_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val wordId = backStackEntry.arguments?.getInt(Routes.ARG_WORD_ID) ?: return@composable
            WordDetailScreen(
                wordId = wordId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}