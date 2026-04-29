package com.example.ui.screens.userScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ui.items.DetailRow


@Composable
fun UserScreen(
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    when (state) {
        is UserState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UserState.Error -> {
            val message = (state as UserState.Error).message
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка: $message")
            }
        }

        is UserState.Success -> {
            val user = (state as UserState.Success).user
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                DetailRow("ID", user.id.toString())
                DetailRow("Фамилия", user.surname)
                DetailRow("Имя", user.name)
                user.secondName?.let {
                    DetailRow("Отчество", it)
                }
                DetailRow("Телефон", user.phoneNumber)

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { navController.popBackStack() }) {
                    Text("Назад")
                }
            }
        }
    }
}
