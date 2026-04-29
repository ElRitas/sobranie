package com.example.ui.screens.bbkBooks

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ui.common.json.appJson
import com.example.ui.items.BookItem
import com.example.ui.model.BbkModel
import com.example.ui.model.BookModel
import com.example.ui.navigation.Screen


@Composable
fun BbkBooksScreen(navController: NavController) {
    val viewModel: BbkBooksViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
    val bbkJson = savedStateHandle?.get<String>("bbk")
    val bbk = bbkJson?.let {
        appJson.decodeFromString(BbkModel.serializer(), it)
    }!!

    LaunchedEffect(Unit) {
        viewModel.loadBooks()
    }

    when (state) {
        is BbkBooksState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is BbkBooksState.Error -> {
            val error = (state as BbkBooksState.Error).message
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
            }
        }

        is BbkBooksState.Success -> {
            val books = (state as BbkBooksState.Success).books
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
            ) {
                item {
                    Text(
                        text = "Книги с ББК ${bbk.code} - ${bbk.description}",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                items(books) { book ->
                    BookItem(book) {
                        val bookJson = appJson.encodeToString(BookModel.serializer(), book)
                        navController.currentBackStackEntry?.savedStateHandle?.set("book", bookJson)
                        navController.navigate(Screen.BookDetail.route)
                    }
                }
            }
        }
    }
}
