package com.w2051756.movieapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.w2051756.movieapp.model.Movie


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MovieDetails(movie: Movie) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DetailText("Title", movie.title)
        DetailText("Year", movie.year)
        DetailText("Rated", movie.rated)
        DetailText("Released", movie.released)
        DetailText("Runtime", movie.runtime)
        DetailText("Genre", movie.genre)
        DetailText("Director", movie.director)
        DetailText("Writer", movie.writer)
        DetailText("Actors", movie.actors)
        DetailText("Plot", movie.plot)
    }
}

@Composable
fun DetailText(label: String, value: String) {
    Text(
        text = "$label: $value",
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp
    )
}

