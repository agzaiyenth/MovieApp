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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color.LightGray,
        contentColor = Color.Black
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Search by Partial Title",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Enter partial title") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
        )

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
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors
        ) {
            Text("Search Titles", fontSize = 18.sp)
        }

        Button(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors
        ) {
            Text("Back to Home", fontSize = 18.sp)
        }

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (hasSearched && results.isEmpty() && query.isNotBlank()) {
                    item {
                        Text(
                            text = "No results found.",
                            fontSize = 16.sp,
                            color = Color.Red
                        )
                    }
                }

                items(results) { movie ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "${movie.title} (${movie.year})",
                            fontSize = 18.sp
                        )
                        Divider(thickness = 2.dp, color = Color.DarkGray)
                    }
                }
            }
        }
    }
}
