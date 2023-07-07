package com.uberalles.whatsappquickchat.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.uberalles.whatsappquickchat.MainActivity
import com.uberalles.whatsappquickchat.MainViewModel
import com.uberalles.whatsappquickchat.ui.history.HistoryPage
import com.uberalles.whatsappquickchat.ui.home.HomePage

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomePage(
                navController = navController,
                viewModel = viewModel,
                mainActivity = MainActivity()
            )
        }
        composable(Screen.History.route) {
            HistoryPage( viewModel = viewModel)
        }
    }
}