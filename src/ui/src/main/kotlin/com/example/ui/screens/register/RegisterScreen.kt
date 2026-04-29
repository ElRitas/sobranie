package com.example.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ui.navigation.Screen

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val form = viewModel.userForm

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Регистрация", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = form.surname,
            onValueChange = { viewModel.userForm = form.copy(surname = it) },
            label = { Text("Фамилия") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = form.name,
            onValueChange = { viewModel.userForm = form.copy(name = it) },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = form.secondName,
            onValueChange = { viewModel.userForm = form.copy(secondName = it) },
            label = { Text("Отчество") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = form.phoneNumber,
            onValueChange = { viewModel.userForm = form.copy(phoneNumber = it) },
            label = { Text("Телефон") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = form.password,
            onValueChange = { viewModel.userForm = form.copy(password = it) },
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.registerUser {
                    navController.navigate(Screen.BookList.route) {
                        popUpTo("register") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрироваться")
        }

        Spacer(Modifier.height(16.dp))

        when (state) {
            is RegisterState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            is RegisterState.Error -> {
                Text(
                    text = (state as RegisterState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }

            is RegisterState.Success -> {
                Text(
                    text = (state as RegisterState.Success).message,
                    color = MaterialTheme.colorScheme.primary
                )
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }

            RegisterState.Idle -> {}
        }
    }
}
