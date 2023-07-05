package com.uberalles.whatsappquickchat.navigation

sealed class Screen (val route: String){
    object Home: Screen("home")
    object History: Screen("history")
}