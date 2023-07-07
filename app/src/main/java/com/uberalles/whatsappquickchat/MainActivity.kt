package com.uberalles.whatsappquickchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.uberalles.whatsappquickchat.database.HistoryDao
import com.uberalles.whatsappquickchat.database.HistoryDatabase
import com.uberalles.whatsappquickchat.navigation.NavGraph
import com.uberalles.whatsappquickchat.ui.theme.WhatsappQuickChatTheme

class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    lateinit var viewModel: MainViewModel
    lateinit var historyDao: HistoryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhatsappQuickChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController = rememberNavController()
                    historyDao = HistoryDatabase.getInstance(application).historyDao()
                    viewModel = MainViewModel()

                    NavGraph(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}




