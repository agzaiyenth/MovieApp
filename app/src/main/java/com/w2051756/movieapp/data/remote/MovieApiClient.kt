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
    val apiKey = "4c7db8cd"
    fun searchMovies(query: String): List<MovieShort> {

        val results = mutableListOf<MovieShort>()
        var page = 1
        val maxPages = 5

        try {
            while (page <= maxPages) {
                Log.d("MovieApiClient", "Searching for query: '$query' on page $page")
                val urlString = "https://www.omdbapi.com/?s=${query}&page=$page&apikey=$apiKey"
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = reader.readText()
                Log.d("MovieApiClient", "Page $page response: $response")
                reader.close()

                val json = JSONObject(response)

                if (json.getString("Response") == "True") {
                    val searchArray = json.getJSONArray("Search")
                    for (i in 0 until searchArray.length()) {
                        val item = searchArray.getJSONObject(i)
                        val title = item.getString("Title")
                        val year = item.getString("Year")

                        if (title.contains(query, ignoreCase = true)) {
                            results.add(MovieShort(title, year))
                        }
                    }
                    page++
                } else {
                    break
                }
            }
        } catch (e: Exception) {
            Log.e("MovieApiClient", "Exception: ${e.message}")
        }

        return results
    }



    fun fetchMovieByTitle(title: String): Movie? {
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
            //response
            //title-chimpanzee
            //"year"-"inasd"
            //rated-3.5


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
