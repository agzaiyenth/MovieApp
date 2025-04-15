package com.w2051756.movieapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { /* TODO: Navigate to Add Movies Screen */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Movies to DB")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TODO: Navigate to Search for Movies Screen */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search for Movies")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TODO: Navigate to Search for Actors Screen */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search for Actors")
            }
        }
    }
}
