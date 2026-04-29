package com.example.ui.network

import com.example.ui.network.dto.LoginRequest
import com.example.ui.network.dto.UserDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class AuthApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun login(request: LoginRequest): UserDto {
        val response = client.post("http://10.0.2.2:8080/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return response.body()
    }
}
