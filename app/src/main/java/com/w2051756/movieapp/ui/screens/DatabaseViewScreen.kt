package com.w2051756.movieapp.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.w2051756.movieapp.data.local.MovieDatabase
import com.w2051756.movieapp.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DatabaseViewScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DatabaseScreenUI()
                }

        }
    }
}

@Composable
fun DatabaseScreenUI() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }

    LaunchedEffect(true) {
        coroutineScope.launch(Dispatchers.IO) {
            val db = Room.databaseBuilder(
                context,
                MovieDatabase::class.java,
                "movie_database"
            ).build()
            movies = db.movieDao().getAllMovies()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Movies in Local Database",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (movies.isEmpty()) {
            Text("No movies found in the database.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(movies) { movie ->
                    MovieRow(movie)
                }
            }
        }
    }
}

@Composable
fun MovieRow(movie: Movie) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Title: ${movie.title}", style = MaterialTheme.typography.bodyLarge)
            Text("Year: ${movie.year}", style = MaterialTheme.typography.bodySmall)
            Text("Genre: ${movie.genre}", style = MaterialTheme.typography.bodySmall)
            Text("Director: ${movie.director}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
