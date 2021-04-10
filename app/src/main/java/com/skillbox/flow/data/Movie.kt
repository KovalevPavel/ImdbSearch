package com.skillbox.flow.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.skillbox.flow.database.MovieContracts
import com.skillbox.flow.database.MovieTypeConverter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Entity(tableName = MovieContracts.MOVIE_TABLE)
@TypeConverters(MovieTypeConverter::class)
@JsonClass(generateAdapter = true)
@Parcelize
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = MovieContracts.Columns.ID)
    @Json(name = "imdbID")
    val _id: String,
    @ColumnInfo(name = MovieContracts.Columns.TITLE)
    @Json(name = "Title")
    val movieTitle: String,
    @ColumnInfo(name = MovieContracts.Columns.TYPE)
    @Json(name = "Type")
    val movieType: MovieType,
    @ColumnInfo(name = MovieContracts.Columns.YEAR)
    @Json(name = "Year")
    val movieYear: String?,
    @ColumnInfo(name = MovieContracts.Columns.POSTER_EXTERNAL_URL)
    @Json(name = "Poster")
    val posterExternalUrl: String?,
    @ColumnInfo(name = MovieContracts.Columns.POSTER_URI)
    var posterUri: String? = null
) : Parcelable
