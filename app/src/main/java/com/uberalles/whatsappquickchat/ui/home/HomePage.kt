package com.uberalles.whatsappquickchat.ui.home

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.component.getFullPhoneNumber
import com.uberalles.whatsappquickchat.MainActivity
import com.uberalles.whatsappquickchat.MainActivity.Companion.AD_BANNER_ID
import com.uberalles.whatsappquickchat.MainActivity.Companion.AD_INTERSTITIAL_ID
import com.uberalles.whatsappquickchat.MainActivity.Companion.TAG
import com.uberalles.whatsappquickchat.MainActivity.Companion.mInterstitialAd
import com.uberalles.whatsappquickchat.MainViewModel
import com.uberalles.whatsappquickchat.R
import com.uberalles.whatsappquickchat.database.History
import com.uberalles.whatsappquickchat.navigation.Screen
import com.uberalles.whatsappquickchat.ui.theme.RobotoSlab
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel,
    mainActivity: MainActivity
) {
    Scaffold(bottomBar = { BottomBar(modifier = modifier) }) { it ->
        val phoneNumber = rememberSaveable { mutableStateOf("") }
        var message by remember { mutableStateOf("") }
        val context = LocalContext.current

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(state = ScrollState(1), enabled = true),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .width(184.dp)
                    .height(196.dp),
            )
            Spacer(modifier = Modifier.height(26.dp))
            Text(
                text = "WhatsApp\nQuick Message",
                fontFamily = RobotoSlab,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(50.dp))
            TogiCountryCodePicker(
                modifier = Modifier
                    .width(320.dp),
                text = phoneNumber.value,
                onValueChange = { phoneNumber.value = it },
                unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                bottomStyle = false,
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.background,
            )
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                OutlinedTextField(
                    modifier = modifier
                        .width(320.dp),
                    value = message,
                    onValueChange = { message = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_chat),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            modifier = modifier.padding(bottom = 20.dp),
                            text = "Input your message here (optional)",
                            textAlign = TextAlign.Center,
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = false,
                    maxLines = 4,
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                FilledIconButton(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(175.dp)
                        .height(50.dp),
                    onClick = {
                        interstitialAds(
                            context = context,
                            onDismiss = {
                                sendWhatsAppMessage(
                                    message = message,
                                    phoneNumber = phoneNumber.value,
                                    context = context,
                                    viewModel = viewModel,
                                )
                            }
                        )

                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = Color(0xFF88CAE2)
                    ),
                    enabled = phoneNumber.value.isNotEmpty() && phoneNumber.value.length > 2,
                    content = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = "Send",
                                textAlign = TextAlign.Center,
                                fontFamily = RobotoSlab,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                            )
                            Icon(
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(30.dp)
                                    .align(Alignment.CenterVertically),
                                painter = painterResource(id = R.drawable.ic_send),
                                contentDescription = null
                            )
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            ElevatedButton(
                modifier = Modifier
                    .height(50.dp)
                    .width(200.dp),
                onClick = { navController.navigate(Screen.History.route) }
            ) {
                Text(
                    text = "Phone Number History",
                    fontFamily = RobotoSlab,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Made with ❤️ by Uberalles",
                fontFamily = RobotoSlab,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

    }
}

private fun sendWhatsAppMessage(
    phoneNumber: String,
    message: String,
    context: Context,
    viewModel: MainViewModel
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
        viewModel.insert(history)

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

private fun getCurrentTime(): String {
    val date = Date()
    val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    return formatter.format(date)
}

@Composable
fun BottomBar(modifier: Modifier) {
    AdMobBanner(modifier = modifier)
}

@Composable
private fun AdMobBanner(
    modifier: Modifier
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = AD_BANNER_ID
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}

private fun interstitialAds(
    context: Context,
    onDismiss: () -> Unit
) {
    InterstitialAd.load(
        context,
        AD_INTERSTITIAL_ID,
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                interstitialAd.show(context as Activity)
                mInterstitialAd = interstitialAd

                mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(TAG, "Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        Log.d(TAG, "Ad dismissed fullscreen content.")
                        onDismiss()
                        mInterstitialAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.e(TAG, "Ad failed to show fullscreen content.")
                        Toast.makeText(
                            context,
                            "Ad failed to show.",
                            Toast.LENGTH_SHORT
                        ).show()
                        mInterstitialAd = null
                    }
                }
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                Log.d("TAG", loadAdError.message)
                mInterstitialAd = null
            }

        })
}



