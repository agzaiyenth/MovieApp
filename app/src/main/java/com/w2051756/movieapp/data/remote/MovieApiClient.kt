package com.w2051756.movieapp.data.remote

import android.util.Log
import com.w2051756.movieapp.model.Movie
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object MovieApiClient {
    fun fetchMovieByTitle(title: String, apiKey: String): Movie? {
        val urlString = "https://www.omdbapi.com/?t=${title.trim()}&apikey=$apiKey"
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()

            val json = JSONObject(response.toString())

            if (json.getString("Response") == "True") {
                return Movie(
                    title = json.getString("Title"),
                    year = json.getString("Year"),
                    rated = json.getString("Rated"),
                    released = json.getString("Released"),
                    runtime = json.getString("Runtime"),
                    genre = json.getString("Genre"),
                    director = json.getString("Director"),
                    writer = json.getString("Writer"),
                    actors = json.getString("Actors"),
                    plot = json.getString("Plot")
                )
            } else {
                Log.e("MovieApiClient", "Movie not found: ${json.getString("Error")}")
            }
        } catch (e: Exception) {
            Log.e("MovieApiClient", "Exception: ${e.message}")
        }

        return null
    }
}
