package com.example.ui.screens.formScreen.addEntity.addFormScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ui.common.json.appJson
import com.example.ui.component.EntitySelector
import com.example.ui.model.AuthorModel
import com.example.ui.model.BbkModel
import com.example.ui.model.PublisherModel
import com.example.ui.navigation.Screen
import com.example.ui.screens.formScreen.addEntity.AddEntityViewModel

@Composable
fun BookFormScreen(viewModel: AddEntityViewModel, navController: NavController) {
    val form = viewModel.bookForm
    val errors = if (viewModel.submitted) form.validate() else emptyMap<String, String>()

    // --- Автор ---
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
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

    Column {
        OutlinedTextField(
            value = form.title,
            onValueChange = { viewModel.bookForm = form.copy(title = it) },
            label = { Text("Название книги*") },
            isError = form.title.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )
        errors["title"]?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        AuthorSelector(
            authors = form.authors,
            onAddClick = { navController.navigate(Screen.SelectAuthor.route) },
            onRemoveClick = { author ->
                viewModel.bookForm = viewModel.bookForm.copy(
                    authors = viewModel.bookForm.authors - author
                )
            }
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
                if (it.all { ch -> ch.isDigit() })
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
        errors["bbk"]?.let { Text(it, color = MaterialTheme.colorScheme.error) }

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
                viewModel.bookForm = form.copy(
                    copies = newCopies,
                    availableCopies = newCopies
                )
            },
            label = { Text("Количество экземпляров*") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = form.copies <= 0,
            modifier = Modifier.fillMaxWidth()
        )
        errors["copies"]?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}

@Composable
fun AuthorSelector(
    authors: List<AuthorModel>,
    onAddClick: () -> Unit,
    onRemoveClick: (AuthorModel) -> Unit
) {
    Column {
        Text("Авторы*", style = MaterialTheme.typography.bodyLarge)

        if (authors.isEmpty()) {
            Text("Не выбрано", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                authors.forEach { author ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(author.name, style = MaterialTheme.typography.bodyMedium)
                        IconButton(onClick = { onRemoveClick(author) }) {
                            Icon(Icons.Default.Close, contentDescription = "Удалить")
                        }
                    }
                }
            }
        }

        OutlinedButton(
            onClick = onAddClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Добавить автора")
            Spacer(Modifier.width(8.dp))
            Text("Добавить автора")
        }
    }
}
