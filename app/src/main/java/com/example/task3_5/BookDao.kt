package com.example.task3_5

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<Book>)

    @androidx.room.Query("SELECT * FROM Book")
    suspend fun getAllBooks(): List<Book>
}