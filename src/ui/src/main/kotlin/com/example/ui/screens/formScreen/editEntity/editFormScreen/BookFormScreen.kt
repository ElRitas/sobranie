package com.example.ui.screens.formScreen.editEntity.editFormScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.example.ui.common.json.appJson
import com.example.ui.component.EntitySelector
import com.example.ui.model.AuthorModel
import com.example.ui.model.BbkModel
import com.example.ui.model.BookModel
import com.example.ui.model.PublisherModel
import com.example.ui.navigation.Screen
import com.example.ui.screens.formScreen.editEntity.EditEntityViewModel
import com.example.ui.screens.formScreen.mapping.BookMapper

@Composable
fun BookFormScreen(viewModel: EditEntityViewModel, navController: NavController) {
    val form = viewModel.bookForm

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    // --- Книга ---
    val bookJson = savedStateHandle?.get<String>("selectedBook")
    val selectedBook = bookJson?.let {
        appJson.decodeFromString(BookModel.serializer(), it)
    }

    // Если выбрали автора из поиска — обновляем форму
    LaunchedEffect(selectedBook) {
        selectedBook?.let { book ->
            if (book.id != form.id) {
                viewModel.bookForm = BookMapper.toForm(book)
            }
            viewModel.buttonVisibility = true
        }
    }

    // --- Автор ---
    val authorJson = savedStateHandle?.get<String>("selectedAuthor")
    val selectedAuthor = authorJson?.let {
        appJson.decodeFromString(AuthorModel.serializer(), it)
    }

    LaunchedEffect(selectedAuthor) {
        selectedAuthor?.let { author ->
            if (author !in viewModel.bookForm.authors) {
                viewModel.bookForm = viewModel.bookForm.copy(
                    authors = viewModel.bookForm.authors + author
                )
            }
        }
    }

    // --- ББК ---
    val bbkJson = savedStateHandle?.get<String>("selectedBbk")
    val selectedBbk = bbkJson?.let {
        appJson.decodeFromString(BbkModel.serializer(), it)
    }
    LaunchedEffect(selectedBbk) {
        selectedBbk?.let { bbk ->
            if (viewModel.bookForm.bbk != bbk) {
                viewModel.bookForm = viewModel.bookForm.copy(
                    bbk = bbk
                )
            }
        }
    }

    // --- Издатель ---
    val publisherJson = savedStateHandle?.get<String>("selectedPublisher")
    val selectedPublisher = publisherJson?.let {
        appJson.decodeFromString(PublisherModel.serializer(), it)
    }
    LaunchedEffect(selectedPublisher) {
        selectedPublisher?.let { publisher ->
            if (viewModel.bookForm.publisher != publisher) {
                viewModel.bookForm = viewModel.bookForm.copy(
                    publisher = publisher
                )
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        EntitySelector(
            label = "Книга",
            showingValue = form.title,
            isError = false,
            onSelectClick = { navController.navigate(Screen.SelectBook.route) }
        )
        if (selectedBook != null) {

            OutlinedTextField(
                value = form.title,
                onValueChange = { viewModel.bookForm = form.copy(title = it) },
                label = { Text("Название книги*") },
                isError = form.title.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            EntitySelector(
                label = "Автор",
                showingValue = form.authors.joinToString { it.name },
                isError = false,
                onSelectClick = { navController.navigate(Screen.SelectAuthor.route) },
            )
            OutlinedTextField(
                value = form.annotation,
                onValueChange = { viewModel.bookForm = form.copy(annotation = it) },
                label = { Text("Аннотация") },
                modifier = Modifier.fillMaxWidth()
            )
            EntitySelector(
                label = "Издатель",
                showingValue = form.publisher?.name ?: "",
                isError = false,
                onSelectClick = { navController.navigate(Screen.SelectPublisher.route) },
            )
            OutlinedTextField(
                value = form.publicationYear,
                onValueChange = {
                    viewModel.bookForm = form.copy(publicationYear = it)
                },
                label = { Text("Год издания") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.codeISBN,
                onValueChange = { viewModel.bookForm = form.copy(codeISBN = it) },
                label = { Text("ISBN") },
                modifier = Modifier.fillMaxWidth()
            )
            EntitySelector(
                label = "ББК*",
                showingValue = form.bbk?.code ?: "",
                isError = form.bbk == null,
                onSelectClick = { navController.navigate(Screen.SelectBbk.route) },
            )
            OutlinedTextField(
                value = form.mediaType,
                onValueChange = { viewModel.bookForm = form.copy(mediaType = it) },
                label = { Text("Формат выпуска") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.volume,
                onValueChange = { viewModel.bookForm = form.copy(volume = it) },
                label = { Text("Объем (стр.)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.language,
                onValueChange = { viewModel.bookForm = form.copy(language = it) },
                label = { Text("Язык") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.originalLanguage,
                onValueChange = { viewModel.bookForm = form.copy(originalLanguage = it) },
                label = { Text("Язык оригинала") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.copies.toString(),
                onValueChange = { input ->
                    val newCopies = input.toIntOrNull() ?: 0

                    // если увеличили количество экземпляров — увеличиваем availableCopies
                    val newAvailable = if (newCopies > form.copies) {
                        form.availableCopies + (newCopies - form.copies)
                    } else if (newCopies < form.availableCopies) {
                        // нельзя чтобы доступных было больше, чем всего
                        newCopies
                    } else {
                        form.availableCopies
                    }

                    viewModel.bookForm = form.copy(
                        copies = newCopies,
                        availableCopies = newAvailable
                    )
                },
                label = { Text("Количество экземпляров*") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = form.copies <= 0,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
