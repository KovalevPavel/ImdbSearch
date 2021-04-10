package com.skillbox.flow.database

object MovieContracts {
    const val MOVIE_TABLE = "movies"

    object Columns {
        const val ID = "id"
        const val TITLE = "title"
        const val TYPE = "type"
        const val YEAR = "year"
        const val POSTER_EXTERNAL_URL = "poster_external_url"
        const val POSTER_URI = "poster_uri"
    }
}
