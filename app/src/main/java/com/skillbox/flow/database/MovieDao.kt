package com.skillbox.flow.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.skillbox.flow.data.Movie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addMovie(movies: List<Movie>)

    @Query("select * from ${MovieContracts.MOVIE_TABLE}")
    fun getAllMovies(): Flow<List<Movie>>

    @Query("select * from ${MovieContracts.MOVIE_TABLE} where ${MovieContracts.Columns.TITLE} like :title and ${MovieContracts.Columns.TYPE}=:type")
    fun searchMovies(title: String, type: String): List<Movie>

    @Query("select count (1) from ${MovieContracts.MOVIE_TABLE} where ${MovieContracts.Columns.ID}=:id")
    fun movieExists(id: String): Int
}
