package com.example.ui.screens.profileScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ui.items.DetailRow

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val form = viewModel.userForm

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Профиль пользователя", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        DetailRow("ID", form.id.toString())
        DetailRow("Телефон", form.phoneNumber)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = form.surname,
            onValueChange = { viewModel.userForm = form.copy(surname = it) },
            label = { Text("Фамилия*") },
            isError = form.surname.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = form.name,
            onValueChange = { viewModel.userForm = form.copy(name = it) },
            label = { Text("Имя*") },
            isError = form.name.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = form.secondName,
            onValueChange = { viewModel.userForm = form.copy(secondName = it) },
            label = { Text("Отчество") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = form.email,
            onValueChange = { viewModel.userForm = form.copy(email = it) },
            label = { Text("Почта") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = form.password,
            onValueChange = { viewModel.userForm = form.copy(password = it) },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            isError = form.password.isBlank(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { viewModel.updateUser() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сохранить изменения")
        }

        when (state) {
            is ProfileState.Loading -> CircularProgressIndicator(Modifier.padding(16.dp))
            is ProfileState.Error -> Text(
                (state as ProfileState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )

            is ProfileState.Success -> Text(
                "Изменения сохранены",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )

            else -> {}
        }
    }
}
