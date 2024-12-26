package com.example.task3_5

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey val key: String,
    val title: String,
    val authorName: String? = null
)