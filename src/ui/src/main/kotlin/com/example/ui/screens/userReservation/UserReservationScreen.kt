package com.example.ui.screens.userReservation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.common.json.appJson
import com.example.ui.items.ReservationItem
import com.example.ui.model.BookModel
import com.example.ui.navigation.Screen


@Composable
fun UserReservationScreen(
    navController: NavController,
) {
    val viewModel: UserReservationViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadBooks()
    }

    when (state) {
        is UserReservationState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is UserReservationState.Error -> {
            val error = (state as UserReservationState.Error).message
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка: $error", color = MaterialTheme.colorScheme.error)
            }
        }

        is UserReservationState.Success -> {
            val reservations = (state as UserReservationState.Success).reservations
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
            ) {
                item {
                    Text(
                        text = "Забронированные книги",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                items(reservations) { reservation ->
                    ReservationItem(
                        reservation = reservation,
                        onClick = {
                            val bookJson = appJson.encodeToString(
                                BookModel.serializer(),
                                reservation.bookModel
                            )
                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "book",
                                bookJson
                            )
                            navController.navigate(Screen.BookDetail.route)
                        }
                    )
                }
            }
            if (reservations.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "У вас нет забронированных книг",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
