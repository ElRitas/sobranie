package com.example.ui.network

import com.example.ui.network.dto.BookDto
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

class BookApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getBooks(page: Int = 1, pageSize: Int = 20): List<BookDto> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/book") {
            parameter("page", page)
            parameter("pageSize", pageSize)
        }
        return response.body()
    }

    suspend fun getBook(id: UUID): BookDto {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/book/$id")
        return response.body()
    }

    // Сделать пагинацию
    suspend fun getBooksByAuthorId(authorId: UUID): List<BookDto> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/book/search") {
            parameter("authorId", authorId)

        }
        return response.body()
    }

    suspend fun getBooksByBbkId(id: UUID): List<BookDto> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/book/search") {
            parameter("bbkId", id)
        }
        return response.body()
    }

    suspend fun getBooksBySentence(sentence: String): List<BookDto> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/book/search") {
            parameter("q", sentence)
        }
        return response.body()
    }

    suspend fun createBook(bookDto: BookDto) {
        client.post("http://10.0.2.2:8080/book") {
            contentType(ContentType.Application.Json)
            setBody(bookDto)
        }
    }

    suspend fun updateBook(bookDto: BookDto) {
        client.put("http://10.0.2.2:8080/book") {
            contentType(ContentType.Application.Json)
            setBody(bookDto)
        }
    }

    suspend fun deleteBook(id: UUID) {
        client.delete("http://10.0.2.2:8080/book/$id")
    }
}
