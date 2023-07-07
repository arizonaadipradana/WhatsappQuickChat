package com.uberalles.whatsappquickchat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.uberalles.whatsappquickchat.database.HistoryDao
import com.uberalles.whatsappquickchat.database.HistoryDatabase
import com.uberalles.whatsappquickchat.navigation.NavGraph
import com.uberalles.whatsappquickchat.ui.theme.WhatsappQuickChatTheme

class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController
    lateinit var viewModel: MainViewModel
    private lateinit var historyDao: HistoryDao

    companion object {
        var mInterstitialAd: InterstitialAd? = null
        const val TAG = "MainActivity"
        const val AD_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"
        const val AD_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WhatsappQuickChatTheme {
                navController = rememberNavController()
                historyDao = HistoryDatabase.getInstance(application).historyDao()
                viewModel = MainViewModel()

                NavGraph(
                    navController = navController,
                    viewModel = viewModel,
                )

                MobileAds.initialize(this)
            }
        }
    }


}




