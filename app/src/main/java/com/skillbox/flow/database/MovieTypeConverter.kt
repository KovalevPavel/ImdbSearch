package com.skillbox.flow.database

import androidx.room.TypeConverter
import com.skillbox.flow.data.MovieType

class MovieTypeConverter {
    @TypeConverter
    fun convertMovieTypeToString(movieType: MovieType) = movieType.name

    @TypeConverter
    fun convertStringToMovieType(type: String) = MovieType.valueOf(type)
}
