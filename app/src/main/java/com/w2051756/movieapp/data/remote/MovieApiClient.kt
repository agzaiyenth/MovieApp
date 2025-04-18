package com.w2051756.movieapp.data.remote

import android.util.Log
import com.w2051756.movieapp.model.Movie
import com.w2051756.movieapp.model.MovieShort
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object MovieApiClient {
    fun searchMovies(query: String): List<MovieShort> {
        val urlString = "https://www.omdbapi.com/?s=${query}&apikey=4c7db8cd"
        val results = mutableListOf<MovieShort>()

        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = reader.readText()
            Log.d("MovieApiClient", "OMDb search response: $response")
            reader.close()

            val json = JSONObject(response)
            if (json.getString("Response") == "True") {
                val searchArray = json.getJSONArray("Search")
                for (i in 0 until searchArray.length()) {
                    val item = searchArray.getJSONObject(i)
                    val title = item.getString("Title")
                    val year = item.getString("Year")
                    results.add(MovieShort(title, year))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        return results
    }

    fun fetchMovieByTitle(title: String): Movie? {
        val urlString = "https://www.omdbapi.com/?t=${title.trim()}&apikey=4c7db8cd"
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
