// com/w2051756/movieapp/ui/screens/HomeScreen.kt
package com.w2051756.movieapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.w2051756.movieapp.data.local.MovieDatabase
import com.w2051756.movieapp.data.local.hardcodedMovies
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(onNavigate: (Screen) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        val db = MovieDatabase.getDatabase(context)
                        db.movieDao().insertAll(hardcodedMovies)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Movies to DB")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onNavigate(Screen.SEARCH_MOVIE) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search for Movies")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onNavigate(Screen.SEARCH_ACTOR) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search for Actors")
            }

        }
    }
}
