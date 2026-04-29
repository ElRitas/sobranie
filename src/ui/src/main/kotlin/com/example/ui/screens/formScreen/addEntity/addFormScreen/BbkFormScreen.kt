package com.example.ui.screens.formScreen.addEntity.addFormScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ui.screens.formScreen.addEntity.AddEntityViewModel

@Composable
fun BbkFormScreen(viewModel: AddEntityViewModel) {
    val form = viewModel.bbkForm
    val errors = if (viewModel.submitted) form.validate() else emptyMap<String, String>()

    Column {
        OutlinedTextField(
            value = form.code,
            onValueChange = { viewModel.bbkForm = form.copy(code = it) },
            label = { Text("ББК код*") },
            modifier = Modifier.fillMaxWidth(),
            isError = form.code.isBlank()
        )
        errors["code"]?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        OutlinedTextField(
            value = form.description,
            onValueChange = { viewModel.bbkForm = form.copy(description = it) },
            label = { Text("Описание*") },
            modifier = Modifier.fillMaxWidth(),
            isError = form.description.isBlank()
        )
        errors["description"]?.let { Text(it, color = MaterialTheme.colorScheme.error) }

    }
}
