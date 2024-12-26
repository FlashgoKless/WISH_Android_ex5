package com.example.task3_5

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryApi {
    @GET("/search.json")
    suspend fun searchBooks(
        @Query("title") title: String
    ): OpenLibraryResponse
}

data class OpenLibraryResponse(
    val docs: List<BookResponse>
)

data class BookResponse(
    val key: String,
    val title: String,
    val author_name: List<String>?
)