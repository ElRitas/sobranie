package com.example.ui.network

import com.example.ui.network.dto.QueueDto
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

class QueueApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getQueue(userId: UUID? = null, bookId: UUID? = null): List<QueueDto> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/queue") {
            parameter("userId", userId)
            parameter("bookId", bookId)
        }
        return response.body()
    }

    suspend fun createQueue(queueDto: QueueDto) {
        println(queueDto.createdAt)
        client.post("http://10.0.2.2:8080/queue") {
            contentType(ContentType.Application.Json)
            setBody(queueDto)
        }
    }

    suspend fun updateQueue(queueDto: QueueDto) {
        client.put("http://10.0.2.2:8080/queue") {
            contentType(ContentType.Application.Json)
            setBody(queueDto)
        }
    }

    suspend fun deleteQueue(id: UUID) {
        client.delete("http://10.0.2.2:8080/queue/$id")
    }

    suspend fun getQueueNumber(bookId: UUID, userId: UUID): Int {
        val response: HttpResponse =
            client.get("http://10.0.2.2:8080/queue/position/$bookId/$userId")
        return response.body()
    }
}
