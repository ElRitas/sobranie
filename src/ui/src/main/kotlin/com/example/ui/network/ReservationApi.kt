package com.example.ui.network

import com.example.ui.network.dto.ReservationDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import java.util.UUID
import javax.inject.Inject

class ReservationApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getReservation(userId: UUID? = null, bookId: UUID? = null): List<ReservationDto> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/reservation") {
            parameter("userId", userId)
            parameter("bookId", bookId)
        }
        return response.body()
    }

    suspend fun createReservation(reservationDto: ReservationDto) {
        client.post("http://10.0.2.2:8080/reservation") {
            contentType(ContentType.Application.Json)
            setBody(reservationDto)
        }
    }

    suspend fun updateReservation(reservationDto: ReservationDto) {
        client.put("http://10.0.2.2:8080/reservation") {
            contentType(ContentType.Application.Json)
            setBody(reservationDto)
        }
    }

    suspend fun deleteReservation(id: UUID) {
        client.delete("http://10.0.2.2:8080/reservation/$id")
    }
}
