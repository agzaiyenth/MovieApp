package com.w2051756.movieapp.ui.screens

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.w2051756.movieapp.data.local.MovieDatabase
import com.w2051756.movieapp.model.Movie
import com.w2051756.movieapp.ui.theme.MovieAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            text = "Search Movies by Actor",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = actorName,
            onValueChange = { actorName = it },
            label = { Text("Enter actor name") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
        )

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
                    if (filtered.isEmpty()) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "No results found.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors
        ) {
            Text("Search", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onBackPressed,
            modifier = Modifier.fillMaxWidth(),
            colors = buttonColors
        ) {
            Text("Back to Home", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (results.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(results) { movie ->
                    MovieDetails(movie = movie)
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(thickness = 2.dp, color = Color.DarkGray)
                }
            }
        } else if (hasSearched && actorName.isNotBlank()) {
            Text(
                text = "No results found.",
                fontSize = 18.sp,
                color = Color.Red
            )
        }
    }
}
