package com.w2051756.movieapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.w2051756.movieapp.model.Movie

@Composable
fun MovieDetails(movie: Movie) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Title: ${movie.title}")
        Text("Year: ${movie.year}")
        Text("Rated: ${movie.rated}")
        Text("Released: ${movie.released}")
        Text("Runtime: ${movie.runtime}")
        Text("Genre: ${movie.genre}")
        Text("Director: ${movie.director}")
        Text("Writer: ${movie.writer}")
        Text("Actors: ${movie.actors}")
        Text("Plot: ${movie.plot}")
    }
}
