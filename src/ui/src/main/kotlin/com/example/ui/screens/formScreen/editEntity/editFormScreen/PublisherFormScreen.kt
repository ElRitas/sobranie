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
import com.example.ui.model.PublisherModel
import com.example.ui.navigation.Screen
import com.example.ui.screens.formScreen.editEntity.EditEntityViewModel
import com.example.ui.screens.formScreen.mapping.PublisherMapper

@Composable
fun PublisherFormScreen(viewModel: EditEntityViewModel, navController: NavController) {
    val form = viewModel.publisherForm
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    // --- Издатель ---
    val publisherJson = savedStateHandle?.get<String>("selectedPublisher")
    val selectedPublisher = publisherJson?.let {
        appJson.decodeFromString(PublisherModel.serializer(), it)
    }

    // Если выбрали автора из поиска — обновляем форму
    LaunchedEffect(selectedPublisher) {
        selectedPublisher?.let { publisher ->
            if (publisher.id != form.id) {
                viewModel.publisherForm = PublisherMapper.toForm(publisher)
            }
            viewModel.buttonVisibility = true
        }
    }


    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        EntitySelector(
            label = "Издатель",
            showingValue = if (form.name.isNotBlank()) form.name else "",
            isError = false,
            onSelectClick = { navController.navigate(Screen.SelectPublisher.route) }
        )
        if (selectedPublisher != null) {

            OutlinedTextField(
                value = form.name,
                onValueChange = { viewModel.publisherForm = form.copy(name = it) },
                label = { Text("Название*") },
                isError = form.name.isBlank(),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.description,
                onValueChange = { viewModel.publisherForm = form.copy(description = it) },
                label = { Text("Описание") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.email,
                onValueChange = { viewModel.publisherForm = form.copy(email = it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = form.phoneNumber,
                onValueChange = { viewModel.publisherForm = form.copy(phoneNumber = it) },
                label = { Text("Телефон") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
