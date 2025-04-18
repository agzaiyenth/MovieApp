package com.w2051756.movieapp.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.w2051756.movieapp.ui.theme.MovieAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.w2051756.movieapp.model.MovieShort
import com.w2051756.movieapp.data.remote.MovieApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SearchByTitleScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
                SearchByTitle(onNavigateBack = { finish() })
            }
        }
    }
}

@Composable
fun SearchByTitle(onNavigateBack: () -> Unit) {
    var query by rememberSaveable { mutableStateOf("") }
    var results by rememberSaveable { mutableStateOf(emptyList<MovieShort>()) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var hasSearched by rememberSaveable { mutableStateOf(false) }



    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Enter partial title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch(Dispatchers.IO) {
                    isLoading = true
                    val movies = MovieApiClient.searchMovies(query.trim())
                    results = movies
                    isLoading = false
                    hasSearched = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search Titles")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                    if (hasSearched && !isLoading && results.isEmpty() && query.isNotBlank()) {
                        item {
                            Text("No results found.")
                        }
                    }
                    items(results) { movie ->
                        Text("${movie.title} (${movie.year})")
                        Divider()
                    }
            }
        }
    }
}
