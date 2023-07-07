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
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import com.uberalles.whatsappquickchat.database.HistoryDao
import com.uberalles.whatsappquickchat.database.HistoryDatabase
import com.uberalles.whatsappquickchat.navigation.NavGraph
import com.uberalles.whatsappquickchat.ui.theme.WhatsappQuickChatTheme

class MainActivity : ComponentActivity(), OnUserEarnedRewardListener {
    lateinit var navController: NavHostController
    lateinit var viewModel: MainViewModel
    private lateinit var historyDao: HistoryDao
    var rewardedInterstitialAd: RewardedInterstitialAd? = null

    companion object {
        var rewardedInterstitialAd: RewardedInterstitialAd? = null
        const val TAG = "MainActivity"
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

                MobileAds.initialize(this){
                    loadAd()
                }
            }
        }
    }

    private fun loadAd() {
        RewardedInterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/5354046379",
            AdRequest.Builder().build(),
            object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    Log.d("MainActivity", "Ad was loaded")
                    rewardedInterstitialAd = ad

                    rewardedInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdClicked() {
                                // Called when a click is recorded for an ad.
                                Log.d(TAG, "Ad was clicked.")
                            }

                            override fun onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                Log.d(TAG, "Ad dismissed fullscreen content.")
                                rewardedInterstitialAd = null
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when ad fails to show.
                                Log.e(TAG, "Ad failed to show fullscreen content.")
                                rewardedInterstitialAd = null
                            }

                            override fun onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d(TAG, "Ad recorded an impression.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad showed fullscreen content.")
                            }
                        }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.d("MainActivity", "$error")
                    rewardedInterstitialAd = null
                }

            }
        )
    }


    override fun onUserEarnedReward(rewardItem: RewardItem) {
        Log.d(TAG, "User earned reward")

    }
}




