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
import androidx.room.Room
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
    var movie by rememberSaveable { mutableStateOf<Movie?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

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
                    hasSearched = true
                    withContext(Dispatchers.Main) {
                        isLoading = false
                        if (fetchedMovie != null) {
                            movie = fetchedMovie
                        } else {
                            movie = null
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
                        Room.databaseBuilder(
                            context,
                            MovieDatabase::class.java,
                            "movie_database"
                        ).build().movieDao().insertMovie(it)
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
                Spacer(modifier = Modifier.height(14.dp))
                MovieDetails(movie = movie!!)
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
