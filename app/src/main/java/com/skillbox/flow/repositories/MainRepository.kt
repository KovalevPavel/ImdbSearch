package com.skillbox.flow.repositories

import android.content.Intent
import android.net.Uri
import com.skillbox.flow.R
import com.skillbox.flow.data.Movie
import com.skillbox.flow.data.MovieType
import com.skillbox.flow.database.Database
import com.skillbox.flow.network.Network
import com.squareup.moshi.JsonDataException
import kotlinx.coroutines.flow.Flow

class MainRepository(private val storeRepo: StoreRepository) {
    private val movieDao = Database.instance.movieDao()

    fun observeMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies()
    }

    suspend fun requestMovies(requestPair: Pair<String, MovieType>): List<Movie> {
        val newList = try {
            Network.api.makeRequest(requestPair.first, requestPair.second.type)
                .execute().body()?.itemsList ?: emptyList()
        } catch (e: JsonDataException) {
            emptyList()
        }

        // проверяем наличие фильма в базе данных. Если он уже есть, выбрасываем из результатов поиска
        newList.filter {
            movieDao.movieExists(it._id) == 0
        }.forEach {
            it.posterUri = storeRepo.savePoster(it)
        }
        movieDao.addMovie(newList)
        return searchMovies(requestPair)
    }

    fun requestFromDatabase(requestPair: Pair<String, MovieType>): List<Movie> {
        return searchMovies(requestPair)
    }

    private fun searchMovies(requestPair: Pair<String, MovieType>): List<Movie> {
        return movieDao.searchMovies("%${requestPair.first}%", requestPair.second.name)
    }

    fun getMovieType(radioButtonId: Int): MovieType {
        return when (radioButtonId) {
            R.id.radioMovie -> MovieType.MOVIE
            R.id.radioSeries -> MovieType.SERIES
            R.id.radioEpisode -> MovieType.EPISODE
            else -> MovieType.UNKNOWN
        }
    }

    fun getBrowserIntent(movieId: String): Intent {
        val uri = Uri.parse("${Network.REQUEST_URI}$movieId/")
        return Intent(Intent.ACTION_VIEW, uri)
    }
}
