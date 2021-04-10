package com.skillbox.flow.data

import com.squareup.moshi.Json

enum class MovieType(val type: String) {
    @Json(name = "movie")
    MOVIE("movie"),

    @Json(name = "series")
    SERIES("series"),

    @Json(name = "episode")
    EPISODE("episode"),
    UNKNOWN("")
}
