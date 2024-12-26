package com.example.task3_5

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.room.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "book-database"
        ).build()
        val bookDao = db.bookDao()
        val viewModelFactory = BookViewModelFactory(bookDao)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(BookViewModel::class.java)

        setContent {
            MaterialTheme {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: BookViewModel) {
    var query by remember { mutableStateOf("") }
    val books by remember { mutableStateOf(viewModel.books) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        BasicTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            decorationBox = { innerTextField ->
                Box(modifier = Modifier.padding(8.dp)) {
                    if (query.isEmpty()) {
                        Text(text = stringResource(id = R.string.hint_enter_title), style = MaterialTheme.typography.bodySmall)
                    }
                    innerTextField()
                }
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            if (query.isNotBlank()) {
                viewModel.searchBooks(query)
            } else {
                Toast.makeText(
                    context,
                    "Ошибка, введите коректное название",

                    Toast.LENGTH_SHORT
                ).show()
            }
        }, modifier = Modifier.padding(8.dp)) {
            Text(text = stringResource(id = R.string.button_search))
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(books) { book ->
                Column(modifier = Modifier.padding(bottom = 8.dp)) {
                    Text(
                        text = stringResource(
                            id = R.string.book_title, book.title
                        ),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(
                            id = R.string.book_author, book.authorName
                                ?: stringResource(id = R.string.unknown_author)
                        ),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

    }
}
