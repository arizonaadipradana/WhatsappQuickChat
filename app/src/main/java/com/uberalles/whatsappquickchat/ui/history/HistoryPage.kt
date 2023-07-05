package com.uberalles.whatsappquickchat.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.uberalles.whatsappquickchat.MainViewModel
import com.uberalles.whatsappquickchat.R
import com.uberalles.whatsappquickchat.database.History

@Composable
fun HistoryPage(
    navController: NavController,
    viewModel: MainViewModel
) {
    val history by viewModel.getAll().observeAsState(listOf())
    Scaffold(topBar = { AppBar(viewModel) }) {
        Box(modifier = Modifier.padding(it)) {
            LazyColumn {
                items(history) { history ->
                    ListHistory(
                        phoneNumber = history.phoneNumber
                    )
                }
            }
        }

    }
}

@Composable
fun ListHistory(
    phoneNumber: String
) {
    Card(
        modifier = Modifier
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .height(50.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(10.dp))
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.align(Center),
                    text = phoneNumber,
                    fontSize = 24.sp
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    viewModel: MainViewModel
) {
    TopAppBar(
        modifier = Modifier,
        title = { Text(text = "Recent History") },
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


//@Preview(showBackground = true, device = Devices.PIXEL_4)
//@Composable
//fun HistoryPagePreview() {
//    HistoryPage(navController = rememberNavController(), viewModel = MainViewModel(Application()))
//}