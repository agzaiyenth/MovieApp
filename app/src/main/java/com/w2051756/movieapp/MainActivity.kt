// com/w2051756/movieapp/MainActivity.kt
package com.w2051756.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.w2051756.movieapp.ui.screens.HomeScreen
import com.w2051756.movieapp.ui.screens.Screen
import com.w2051756.movieapp.ui.screens.SearchMovieScreen
import com.w2051756.movieapp.ui.theme.MovieAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
                var currentScreen by rememberSaveable { mutableStateOf(Screen.HOME) }

                when (currentScreen) {
                    Screen.HOME -> HomeScreen(onNavigate = { newScreen -> currentScreen = newScreen })
                    Screen.SEARCH_MOVIE -> SearchMovieScreen(onNavigateBack = { currentScreen = Screen.HOME })
                }
            }
        }
    }
}
