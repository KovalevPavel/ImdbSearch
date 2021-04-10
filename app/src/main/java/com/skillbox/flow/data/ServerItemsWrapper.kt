package com.skillbox.flow.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ServerItemsWrapper(
    @Json(name = "Search")
    val itemsList: List <Movie>
)
