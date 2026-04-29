package com.example.ui.network

import com.example.ui.network.dto.AuthorDto
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

class AuthorApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getAuthor(id: UUID): AuthorDto {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/author/$id")
        return response.body()
    }

    suspend fun getAuthor(name: String): List<AuthorDto> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/author/by-name") {
            parameter("name", name)
        }
        return response.body()
    }

    suspend fun createAuthor(authorDto: AuthorDto) {
        client.post("http://10.0.2.2:8080/author") {
            contentType(ContentType.Application.Json)
            setBody(authorDto)
        }
    }

    suspend fun updateAuthor(authorDto: AuthorDto) {
        client.put("http://10.0.2.2:8080/author") {
            contentType(ContentType.Application.Json)
            setBody(authorDto)
        }
    }

    suspend fun deleteAuthor(id: UUID) {
        client.delete("http://10.0.2.2:8080/author/$id")
    }
}
