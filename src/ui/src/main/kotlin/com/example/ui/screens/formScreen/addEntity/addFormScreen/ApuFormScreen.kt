package com.example.ui.screens.formScreen.addEntity.addFormScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.screens.formScreen.addEntity.AddEntityViewModel

@Composable
fun ApuFormScreen(viewModel: AddEntityViewModel, navController: NavController) {
    val form = viewModel.apuForm
    val errors = if (viewModel.submitted) form.validate() else emptyMap<String, String>()

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    // --- ББК ---
    val bbkJson = savedStateHandle?.get<String>("selectedBbk")
    val selectedBbk = bbkJson?.let {
        appJson.decodeFromString(BbkModel.serializer(), it)
    }
    LaunchedEffect(selectedBbk) {
        selectedBbk?.let { bbk ->
            if (viewModel.apuForm.bbk != bbk) {
                viewModel.apuForm = viewModel.apuForm.copy(
                    bbk = bbk
                )
            }
        }
    }

    Column {
        OutlinedTextField(
            value = form.term,
            onValueChange = { viewModel.apuForm = form.copy(term = it) },
            label = { Text("Ключевое слово*") },
            modifier = Modifier.fillMaxWidth(),
            isError = form.term.isBlank()
        )
        errors["term"]?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        EntitySelector(
            label = "ББК*",
            showingValue = form.bbk?.code ?: "",
            isError = form.bbk == null,
            onSelectClick = { navController.navigate(Screen.SelectBbk.route) },
        )
        errors["bbk"]?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}
