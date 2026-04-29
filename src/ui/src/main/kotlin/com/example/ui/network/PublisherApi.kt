package com.example.ui.network

import com.example.ui.network.dto.PublisherDto
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

class PublisherApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getPublisher(id: UUID): PublisherDto {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/publisher/$id")
        return response.body()
    }

    suspend fun getPublisher(name: String): List<PublisherDto> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/publisher/by-name") {
            parameter("name", name)
        }
        return response.body()
    }

    suspend fun createPublisher(publisherDto: PublisherDto) {
        client.post("http://10.0.2.2:8080/publisher") {
            contentType(ContentType.Application.Json)
            setBody(publisherDto)
        }
    }

    suspend fun updatePublisher(publisherDto: PublisherDto) {
        client.put("http://10.0.2.2:8080/publisher") {
            contentType(ContentType.Application.Json)
            setBody(publisherDto)
        }
    }

    suspend fun deletePublisher(id: UUID) {
        client.delete("http://10.0.2.2:8080/publisher/$id")
    }
}
