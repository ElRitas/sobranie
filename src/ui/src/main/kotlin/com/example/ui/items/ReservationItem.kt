package com.example.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.model.ReservationModel

@Composable
fun ReservationItem(reservation: ReservationModel, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            // Заголовок книги
            Text(
                reservation.bookModel.title,
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(Modifier.height(4.dp))

            // Даты брони
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column {
                    Text("Бронь с:", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    Text(
                        reservation.reservationDate.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column {
                    Text(
                        "Действует до:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Text(
                        reservation.cancelDate.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
