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
import com.example.ui.model.BbkModel
import com.example.ui.navigation.Screen
import com.example.ui.screens.formScreen.editEntity.EditEntityViewModel
import com.example.ui.screens.formScreen.mapping.BbkMapper

@Composable
fun BbkFormScreen(viewModel: EditEntityViewModel, navController: NavController) {
    val form = viewModel.bbkForm

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    // --- ББК ---
    val bbkJson = savedStateHandle?.get<String>("selectedBbk")
    val selectedBbk = bbkJson?.let {
        appJson.decodeFromString(BbkModel.serializer(), it)
    }

    // Если выбрали автора из поиска — обновляем форму
    LaunchedEffect(selectedBbk) {
        selectedBbk?.let { bbk ->
            if (bbk.id != form.id) {
                viewModel.bbkForm = BbkMapper.toForm(bbk)
            }
            viewModel.buttonVisibility = true
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        EntitySelector(
            label = "ББК",
            showingValue = if (form.code.isNotBlank()) form.code else "",
            isError = false,
            onSelectClick = { navController.navigate(Screen.SelectBbk.route) }
        )
        if (selectedBbk != null) {

            OutlinedTextField(
                value = form.code,
                onValueChange = { viewModel.bbkForm = form.copy(code = it) },
                label = { Text("ББК код*") },
                modifier = Modifier.fillMaxWidth(),
                isError = form.code.isBlank()
            )
            OutlinedTextField(
                value = form.description,
                onValueChange = { viewModel.bbkForm = form.copy(description = it) },
                label = { Text("Описание*") },
                modifier = Modifier.fillMaxWidth(),
                isError = form.description.isBlank()
            )
        }
    }
}
