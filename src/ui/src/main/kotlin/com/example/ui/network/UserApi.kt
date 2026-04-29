package com.example.ui.network

import com.example.ui.network.dto.BookDto
import com.example.ui.network.dto.UserDto
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

class UserApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getUser(id: UUID): UserDto {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/user/$id")
        return response.body()
    }

    suspend fun getUser(phone: String): List<UserDto> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/user/by-phone") {
            parameter("phone", phone)
        }
        return response.body()
    }

    suspend fun addToFavorite(userId: UUID, bookId: UUID) {
        client.post("http://10.0.2.2:8080/user/$userId/favorite/$bookId")
    }

    suspend fun removeFromFavorite(userId: UUID, bookId: UUID) {
        client.delete("http://10.0.2.2:8080/user/$userId/favorite/$bookId")
    }

    suspend fun getFavorite(userId: UUID): List<BookDto> {
        val response = client.get("http://10.0.2.2:8080/user/$userId/favorite")
        return response.body()
    }

    suspend fun getFavoriteById(id: UUID): List<BookDto> {
        val response = client.get("http://10.0.2.2:8080/user/$id/favorite")
        return response.body()
    }

    suspend fun createUser(userDto: UserDto) {
        client.post("http://10.0.2.2:8080/user") {
            contentType(ContentType.Application.Json)
            setBody(userDto)
        }
    }

    suspend fun updateUser(userDto: UserDto) {
        client.put("http://10.0.2.2:8080/user") {
            contentType(ContentType.Application.Json)
            setBody(userDto)
        }
    }

    suspend fun deleteUser(id: UUID) {
        client.delete("http://10.0.2.2:8080/user/$id")
    }
}
