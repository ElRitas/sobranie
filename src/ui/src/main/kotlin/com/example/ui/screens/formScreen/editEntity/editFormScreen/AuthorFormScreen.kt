package com.example.ui.screens.formScreen.editEntity.editFormScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.ui.common.json.appJson
import com.example.ui.component.EntitySelector
import com.example.ui.model.AuthorModel
import com.example.ui.navigation.Screen
import com.example.ui.screens.formScreen.editEntity.EditEntityViewModel
import com.example.ui.screens.formScreen.mapping.AuthorMapper

@Composable
fun AuthorFormScreen(viewModel: EditEntityViewModel, navController: NavController) {
    val form = viewModel.authorForm

    // Забираем автора из сохранённого состояния после выбора
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val authorJson = savedStateHandle?.get<String>("selectedAuthor")
    val selectedAuthor = authorJson?.let {
        appJson.decodeFromString(AuthorModel.serializer(), it)
    }

    // Если выбрали автора из поиска — обновляем форму
    LaunchedEffect(selectedAuthor) {
        selectedAuthor?.let { author ->
            if (author.id != form.id) {
                viewModel.authorForm = AuthorMapper.toForm(author)
            }
            viewModel.buttonVisibility = true
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Строчка выбора автора (readonly + кнопка поиска)
        EntitySelector(
            label = "Автор",
            showingValue = if (form.name.isNotBlank()) form.name else "",
            isError = false,
            onSelectClick = { navController.navigate(Screen.SelectAuthor.route) }
        )

        if (selectedAuthor != null) {

            // Поле для редактирования имени
            OutlinedTextField(
                value = form.name,
                onValueChange = { viewModel.authorForm = form.copy(name = it) },
                label = { Text("Имя автора*") },
                modifier = Modifier.fillMaxWidth(),
                isError = form.name.isBlank()
            )
        }
    }
}
