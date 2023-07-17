package com.uberalles.whatsappquickchat

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.uberalles.whatsappquickchat.database.HistoryDao
import com.uberalles.whatsappquickchat.database.HistoryDatabase
import com.uberalles.whatsappquickchat.navigation.NavGraph
import com.uberalles.whatsappquickchat.ui.theme.WhatsappQuickChatTheme

class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    lateinit var viewModel: MainViewModel
    private lateinit var historyDao: HistoryDao

    companion object {
        var keepSplashOpened = true
        var mInterstitialAd: InterstitialAd? = null
        const val TAG = "MainActivity"
        const val AD_INTERSTITIAL_ID = "ca-app-pub-3942164860535593/995361858322"
        const val AD_BANNER_ID = "ca-app-pub-3942164860535593/300558377822"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { keepSplashOpened }
        }
        setContent {
            WhatsappQuickChatTheme {
                navController = rememberNavController()
                historyDao = HistoryDatabase.getInstance(application).historyDao()
                viewModel = MainViewModel()

                NavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
                MobileAds.initialize(this)
            }
        }
    }
}




