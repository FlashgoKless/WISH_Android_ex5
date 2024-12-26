package com.example.task3_5

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookViewModel(private val bookDao: BookDao) : ViewModel() {
    private val api = Retrofit.Builder()
        .baseUrl("https://openlibrary.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenLibraryApi::class.java)

    private val _books = mutableStateListOf<Book>()
    val books: List<Book> get() = _books

    fun searchBooks(title: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.searchBooks(title)
                }

                val bookList = response.docs.map {
                    Book(
                        key = it.key,
                        title = it.title,
                        authorName = it.author_name?.joinToString(", ")
                    )
                }

                withContext(Dispatchers.IO) {
                    bookDao.insertBooks(bookList)
                }

                _books.clear()
                _books.addAll(bookList)
            } catch (e: Exception) {
                withContext(Dispatchers.IO) {
                    _books.clear()
                    _books.addAll(bookDao.getAllBooks())
                }
            }
        }
    }
}