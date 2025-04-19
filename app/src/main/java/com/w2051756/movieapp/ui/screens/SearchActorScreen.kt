package com.w2051756.movieapp.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.w2051756.movieapp.data.local.MovieDatabase
import com.w2051756.movieapp.model.Movie
import com.w2051756.movieapp.ui.theme.MovieAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchActorScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
                SearchActorScreenContent(
                    onBackPressed = { finish() }
                )
            }
        }
    }
}

@Composable
fun SearchActorScreenContent(onBackPressed: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var hasSearched by rememberSaveable { mutableStateOf(false) }
    var actorName by rememberSaveable { mutableStateOf("") }
    var results by rememberSaveable { mutableStateOf(emptyList<Movie>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = actorName,
            onValueChange = { actorName = it },
            label = { Text("Enter actor name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    val db = MovieDatabase.getDatabase(context)
                    val allMovies = db.movieDao().getAllMovies()
                    val query = actorName.trim().lowercase()
                    val filtered = allMovies.filter {
                        it.actors.lowercase().contains(query)
                    }
                    hasSearched = true
                    results = filtered
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBackPressed,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (results.isNotEmpty()) {
            LazyColumn {
                items(results) { movie ->
                    MovieDetails(movie = movie)
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider()
                }
            }
        } else if (hasSearched && actorName.isNotBlank()) {
            Text("No results found.")
        }
    }
}
