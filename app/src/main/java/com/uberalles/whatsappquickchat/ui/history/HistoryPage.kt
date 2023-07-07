package com.uberalles.whatsappquickchat.ui.history

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.uberalles.whatsappquickchat.MainViewModel
import com.uberalles.whatsappquickchat.R
import com.uberalles.whatsappquickchat.database.History
import com.uberalles.whatsappquickchat.ui.theme.RobotoSlab

@Composable
fun HistoryPage(
    viewModel: MainViewModel,
) {
    val context = LocalContext.current
    val history by viewModel.getAll().observeAsState(listOf())
    val emptyList by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.empty_list))
    val progress by animateLottieCompositionAsState(
        composition = emptyList,
        iterations = LottieConstants.IterateForever
    )

    if (history.isEmpty()) {
        Scaffold {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(top = 50.dp),
                    fontSize = 24.sp,
                    fontFamily = RobotoSlab,
                    fontWeight = FontWeight.W600,
                    text = "There is nothing here,\ntry to message some numbers first!",
                    textAlign = TextAlign.Center
                )
                LottieAnimation(composition = emptyList, progress = { progress })
            }
        }
    } else {
        Scaffold(topBar = { AppBar(viewModel) }) {
            Box(modifier = Modifier.padding(it)) {

                LazyColumn {
                    items(history) { history ->
                        ListHistory(
                            phoneNumber = history.phoneNumber,
                            message = history.message,
                            createdAt = history.createdAt,
                            onClickSend = {
                                sendAgain(context = context, history = history)
                            }
                        )
                    }
                }

            }
        }
    }

}

@Composable
fun ListHistory(
    phoneNumber: String,
    message: String,
    createdAt: String,
    onClickSend: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = expanded, label = "transition")
    val iconRotation by
    transition.animateFloat(label = "icon change") { state ->
        if (state) {
            0f
        } else {
            180f
        }
    }
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeIn(initialAlpha = .3f, animationSpec = tween(300))
    }
    val exitTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth()
            .clickable {
                expanded = !expanded
            },
        shape = RoundedCornerShape(CornerSize(10.dp)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(250.dp),
                    text = phoneNumber,
                    fontSize = 24.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Message this number",
                        modifier = Modifier
                            .size(30.dp)
                            .rotate(180f)
                            .clickable {
                                onClickSend()
                            }

                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Expand the description",
                        modifier = Modifier
                            .size(48.dp)
                            .rotate(iconRotation)

                    )
                }

            }
            AnimatedVisibility(
                visible = expanded,
                enter = enterTransition,
                exit = exitTransition
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = message,
                        textAlign = TextAlign.Start,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = createdAt
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    viewModel: MainViewModel,
) {
    TopAppBar(
        modifier = Modifier,
        title = { },
        actions = {
            IconButton(
                onClick = {
                    viewModel.deleteAll()
                },
            ) {
                Icon(
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp),
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null
                )
            }


        }
    )
}

private fun sendAgain(
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

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun ListPreview() {
    ListHistory(
        phoneNumber = "+62895355768211",
        onClickSend = { },
        createdAt = "12/12/2021",
        message = "e1s2e1 2c4e31 d1234d "
    )
}