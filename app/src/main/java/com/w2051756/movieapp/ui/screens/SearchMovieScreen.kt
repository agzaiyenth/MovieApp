package com.w2051756.movieapp.ui.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.w2051756.movieapp.ui.theme.MovieAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val scrollState = rememberScrollState()

    var hasSearched by rememberSaveable { mutableStateOf(false) }
    var movieTitle by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    // Saving movie fields individually
    var title by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    var rated by rememberSaveable { mutableStateOf("") }
    var released by rememberSaveable { mutableStateOf("") }
    var runtime by rememberSaveable { mutableStateOf("") }
    var genre by rememberSaveable { mutableStateOf("") }
    var director by rememberSaveable { mutableStateOf("") }
    var writer by rememberSaveable { mutableStateOf("") }
    var actors by rememberSaveable { mutableStateOf("") }
    var plot by rememberSaveable { mutableStateOf("") }

    val movie: Movie? = if (title.isNotBlank()) {
        Movie(
            title = title,
            year = year,
            rated = rated,
            released = released,
            runtime = runtime,
            genre = genre,
            director = director,
            writer = writer,
            actors = actors,
            plot = plot
        )
    } else null

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color.LightGray,
        contentColor = Color.Black
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Search for a Movie",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = movieTitle,
            onValueChange = {
                movieTitle = it
                hasSearched = false
            },
            label = { Text("Enter movie title") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
        )

        Button(
            onClick = {
                isLoading = true
                coroutineScope.launch(Dispatchers.IO) {
                    val fetchedMovie = MovieApiClient.fetchMovieByTitle(movieTitle.text)
                    withContext(Dispatchers.Main) {
                        isLoading = false
                        hasSearched = true
                        if (fetchedMovie != null) {
                            title = fetchedMovie.title
                            year = fetchedMovie.year
                            rated = fetchedMovie.rated
                            released = fetchedMovie.released
                            runtime = fetchedMovie.runtime
                            genre = fetchedMovie.genre
                            director = fetchedMovie.director
                            writer = fetchedMovie.writer
                            actors = fetchedMovie.actors
                            plot = fetchedMovie.plot
                        } else {
                            title = ""; year = ""; rated = ""; released = ""
                            runtime = ""; genre = ""; director = ""
                            writer = ""; actors = ""; plot = ""
                            Toast.makeText(context, "No results found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors
        ) {
            Text("Retrieve Movie", fontSize = 18.sp)
        }

        Button(
            onClick = {
                movie?.let {
                    coroutineScope.launch(Dispatchers.IO) {
                        val db = MovieDatabase.getInstance(context)
                        db.movieDao().insertMovie(it)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Saved to DB", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            enabled = movie != null,
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors
        ) {
            Text("Save movie to Database", fontSize = 18.sp)
        }

        Button(
            onClick = onNavigateBack,
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors
        ) {
            Text("Back to Home", fontSize = 18.sp)
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        } else {
            if (movie != null) {
                MovieDetails(movie = movie)
            } else if (movieTitle.text.isNotBlank() && hasSearched) {
                Text(
                    text = "No results found for \"${movieTitle.text}\"",
                    fontSize = 16.sp,
                    color = Color.Red
                )
            }
        }
    }
}
