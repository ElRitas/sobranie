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
fun AuthorFormScreen(viewModel: AddEntityViewModel) {
    val form = viewModel.authorForm
    val errors = if (viewModel.submitted) form.validate() else emptyMap<String, String>()
    Column {
        OutlinedTextField(
            value = form.name,
            onValueChange = { viewModel.authorForm = form.copy(name = it) },
            label = { Text("Имя автора*") },
            modifier = Modifier.fillMaxWidth(),
            isError = form.name.isBlank()
        )
        errors["name"]?.let { Text(it, color = MaterialTheme.colorScheme.error) }
    }
}
