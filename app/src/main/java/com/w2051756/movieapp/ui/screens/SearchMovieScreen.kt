package com.w2051756.movieapp.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.w2051756.movieapp.ui.theme.MovieAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import kotlinx.coroutines.withContext

class SearchMovieScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieAppTheme {
                SearchMovieScreenContent(onNavigateBack = { finish() })
            }
        }
    }
}

@Composable
fun SearchMovieScreenContent(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var movieTitle by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var movie by rememberSaveable { mutableStateOf<Movie?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

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
                        title = movieTitle.text
                    )
                    withContext(Dispatchers.Main) {
                        movie = fetchedMovie
                        isLoading = false
                    }
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

        Button(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            movie?.let {
                MovieDetails(movie = it)
            }
        }
    }
}
