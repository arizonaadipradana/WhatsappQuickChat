package com.uberalles.whatsappquickchat

import android.app.Activity
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.togitech.ccp.component.getFullPhoneNumber
import com.uberalles.whatsappquickchat.MainActivity.Companion.mInterstitialAd
import com.uberalles.whatsappquickchat.database.History
import com.uberalles.whatsappquickchat.database.HistoryDatabase
import com.uberalles.whatsappquickchat.database.HistoryRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class MainViewModel : ViewModel() {
    private val getAll: LiveData<List<History>>
    private val application: Application = Application()
    private val repository: HistoryRepository

    init {
        val historyDao = HistoryDatabase.getInstance(application).historyDao()
        repository = HistoryRepository(historyDao)
        getAll = repository.getAll
    }

    private fun insert(history: History) = viewModelScope.launch {
        repository.insert(history)
    }

    fun getAll(): LiveData<List<History>> {
        return getAll
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.delete()
        }
    }

    @Composable
    fun BannerAds(
        modifier: Modifier
    ) {
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = MainActivity.AD_BANNER_ID
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }

    fun interstitialAds(
        context: Context,
        onDismiss: () -> Unit
    ) {
        InterstitialAd.load(
            context,
            MainActivity.AD_INTERSTITIAL_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    interstitialAd.show(context as Activity)
                    mInterstitialAd = interstitialAd

                    mInterstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {

                            override fun onAdDismissedFullScreenContent() {
                                Log.d(MainActivity.TAG, "Ad dismissed fullscreen content.")
                                onDismiss()
                                mInterstitialAd = null
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                Log.e(MainActivity.TAG, "Ad failed to show fullscreen content.")
                                Toast.makeText(
                                    context,
                                    "Ad failed to show.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onDismiss()
                                mInterstitialAd = null
                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.d("TAG", loadAdError.message)
                    onDismiss()
                    mInterstitialAd = null
                }

            })
    }

    private fun getCurrentTime(): String {
        val date = Date()
        val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return formatter.format(date)
    }

    fun sendWhatsAppMessage(
        phoneNumber: String,
        message: String,
        context: Context,
    ) {
        if (phoneNumber.isEmpty()) {
            Toast.makeText(
                context,
                "Please input a phone number",
                Toast.LENGTH_SHORT
            ).show()
            return
        } else {
            val trimmedPhoneNumber = getFullPhoneNumber().replace(" ", "")
            val url = "https://wa.me/${trimmedPhoneNumber}?text=${message}"

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse(url)
                `package` = "com.whatsapp"
            }

            val getCurrentTime = getCurrentTime()
            val createdAt = "Send at $getCurrentTime"

            val history = History(
                phoneNumber = getFullPhoneNumber(),
                message = message,
                createdAt = createdAt
            )
            insert(history)

            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "WhatsApp not installed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun sendAgain(
        context: Context,
        history: History
    ) {
        val url = "https://wa.me/${history.phoneNumber}?text=${history.message}"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data =
                Uri.parse(url)
            `package` = "com.whatsapp"
        }
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                context,
                "WhatsApp not installed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}