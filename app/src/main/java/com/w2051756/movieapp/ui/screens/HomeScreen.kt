package com.w2051756.movieapp.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.w2051756.movieapp.data.local.MovieDatabase
import com.w2051756.movieapp.data.local.hardcodedMovies
import com.w2051756.movieapp.ui.screens.SearchActorScreen
import com.w2051756.movieapp.ui.screens.SearchByTitleScreen
import com.w2051756.movieapp.ui.screens.SearchMovieScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Movie App",
                fontSize = 32.sp, // Increased title font size
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            if (isPortrait) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(24.dp), // More even spacing
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HomeButtons(context, coroutineScope, Modifier.fillMaxWidth())
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HomeButtons(context, coroutineScope, Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun HomeButtons(context: android.content.Context, coroutineScope: CoroutineScope, modifier: Modifier) {
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color.LightGray,
        contentColor = Color.Black
    )

    val fontSize = 18.sp // Larger font for all buttons

    Button(
        onClick = {
            coroutineScope.launch {
                val db = MovieDatabase.getDatabase(context)
                db.movieDao().insertAll(hardcodedMovies)
            }
        },
        modifier = modifier,
        colors = buttonColors
    ) {
        Text("Add Movies to DB", fontSize = fontSize)
    }

    Button(
        onClick = {
            context.startActivity(Intent(context, SearchMovieScreen::class.java))
        },
        modifier = modifier,
        colors = buttonColors
    ) {
        Text("Search for Movies", fontSize = fontSize)
    }

    Button(
        onClick = {
            context.startActivity(Intent(context, SearchActorScreen::class.java))
        },
        modifier = modifier,
        colors = buttonColors
    ) {
        Text("Search for Actors", fontSize = fontSize)
    }

    Button(
        onClick = {
            context.startActivity(Intent(context, SearchByTitleScreen::class.java))
        },
        modifier = modifier,
        colors = buttonColors
    ) {
        Text("Search Titles via API", fontSize = fontSize)
    }
}
