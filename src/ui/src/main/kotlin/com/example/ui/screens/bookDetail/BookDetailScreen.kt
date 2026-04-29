package com.example.ui.screens.bookDetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ui.common.enums.UserRole
import com.example.ui.common.json.appJson
import com.example.ui.items.DetailRow
import com.example.ui.model.AuthorModel
import com.example.ui.model.BbkModel
import com.example.ui.model.BookModel
import com.example.ui.navigation.Screen
import com.example.ui.util.UserStore


@Composable
fun BookDetailScreen(
    navController: NavController,
    viewModel: BookDetailViewModel = hiltViewModel()
) {
    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
    val bookJson = savedStateHandle?.get<String>("book")
    val book = bookJson?.let {
        appJson.decodeFromString(BookModel.serializer(), it)
    }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(book) {
        if (book != null) {
            viewModel.setBook(book, UserStore.getId())
        }
    }

    when (state) {
        is BookDetailState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is BookDetailState.Success -> {
            val book = (state as BookDetailState.Success).book
            val actions = (state as BookDetailState.Success).actionsState
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Основная информация о книге
                Text(book.title, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))

                // Авторы
                if (book.authors.isNotEmpty()) {
                    Text("Авторы:", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    book.authors.forEach { author ->
                        Text(
                            text = author.name,
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.clickable {
                                val authorJson =
                                    appJson.encodeToString(AuthorModel.serializer(), author)
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "author",
                                    authorJson
                                )
                                navController.navigate(Screen.AuthorBooks.createRoute(author.id))
                            }
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Аннотация
                Text(
                    text = book.annotation ?: "Аннотация отсутствует",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Детальная информация
                Text("Детальная информация:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                book.publisherModel?.let { publisher ->
                    DetailRow("Издательство:", publisher.name)
                }

                book.publicationYear?.let { year ->
                    DetailRow("Год издания:", year.toString())
                }

                // ISBN
                book.codeISBN?.let { isbn ->
                    DetailRow("ISBN:", isbn)
                }

                // ББК
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "ББК",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.width(150.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = book.bbkModel.code,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable {
                            val bbkJson =
                                appJson.encodeToString(BbkModel.serializer(), book.bbkModel)
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "bbk",
                                bbkJson
                            )
                            navController.navigate(Screen.BbkBooks.createRoute(book.bbkModel.id))
                        }
                    )
                }

                // Тип носителя
                book.mediaType?.let { type ->
                    DetailRow("Тип носителя:", type)
                }

                // Объем
                book.volume?.let { volume ->
                    DetailRow("Объем:", volume)
                }

                // Языки
                DetailRow("Язык:", book.language ?: "не указан")
                book.originalLanguage?.let { lang ->
                    DetailRow("Оригинальный язык:", lang)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Свободные экземпляры: ${book.availableCopies}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    when (UserStore.getRole()) {
                        UserRole.READER -> ReaderButtons(book, actions, viewModel)
                        UserRole.LIBRARIAN -> LibrarianButtons(book, navController)
                        UserRole.MODERATOR -> {}
                        null -> {}
                    }
                }
            }
        }

        is BookDetailState.Error -> {
            val error = (state as BookDetailState.Error).message
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}


// Кнопки читателя
@Composable
private fun ReaderButtons(
    book: BookModel,
    actions: BookActionsState,
    viewModel: BookDetailViewModel
) {
    // Кнопка "избранное"
    Button(
        onClick = {
            viewModel.toggleFavorite(UserStore.getId()!!, book.id)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (actions.isFavorite) MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.primary
        )
    ) {
        Text(if (actions.isFavorite) "Убрать из избранного" else "В избранное")
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Вторая кнопка (бронь / очередь)
    val buttonText: String
    val buttonColor: Color
    val onClick: () -> Unit

    when {
        // Если книга уже забронирована
        actions.isReserved -> {
            buttonText = "Снять бронь"
            buttonColor = MaterialTheme.colorScheme.secondary
            onClick = { viewModel.toggleReservation(UserStore.getId()!!, book.id) }
        }

        // Если есть свободные экземпляры
        book.availableCopies > 0 -> {
            buttonText = "Забронировать"
            buttonColor = MaterialTheme.colorScheme.primary
            onClick = { viewModel.toggleReservation(UserStore.getId()!!, book.id) }
        }

        // Если книга в очереди
        actions.queueNumber != 0 -> {
            buttonText = "Очередь: ${actions.queueNumber}. Выйти?"
            buttonColor = MaterialTheme.colorScheme.secondary
            onClick = { viewModel.toggleQueue(UserStore.getId()!!, book.id) }
        }

        // Если нет свободных → можно встать в очередь
        else -> {
            buttonText = "Встать в очередь"
            buttonColor = MaterialTheme.colorScheme.primary
            onClick = { viewModel.toggleQueue(UserStore.getId()!!, book.id) }
        }
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
    ) {
        Text(buttonText)
    }
}

// Кнопки библиотекаря
@Composable
private fun LibrarianButtons(book: BookModel, navController: NavController) {
    Button(onClick = { navController.navigate(Screen.BookReservation.createRoute(book.id)) }) {
        Text("Посмотреть брони")
    }
    Button(onClick = { navController.navigate(Screen.BookIssuance.createRoute(book.id)) }) {
        Text("Посмотреть выдачи")
    }
}
