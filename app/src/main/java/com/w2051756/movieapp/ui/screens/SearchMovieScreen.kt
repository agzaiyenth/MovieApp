package com.w2051756.movieapp.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.w2051756.movieapp.data.local.MovieDatabase
import com.w2051756.movieapp.data.remote.MovieApiClient
import com.w2051756.movieapp.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SearchMovieScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var movieTitle by remember { mutableStateOf(TextFieldValue("")) }
    var movie by remember { mutableStateOf<Movie?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = movieTitle,
            onValueChange = { movieTitle = it },
            label = { Text("Enter movie title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isLoading = true
                coroutineScope.launch(Dispatchers.IO) {
                    val fetchedMovie = MovieApiClient.fetchMovieByTitle(
                        title = movieTitle.text,
                        apiKey = BuildConfig.OMDB_API_KEY
                    )
                    movie = fetchedMovie
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Retrieve Movie")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                movie?.let {
                    coroutineScope.launch(Dispatchers.IO) {
                        MovieDatabase.getDatabase(context).movieDao().insertMovie(it)
                    }
                }
            },
            enabled = movie != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save movie to Database")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            movie?.let {
                MovieDetails(movie = it)
            }
        }
    }
}
