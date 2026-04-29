package com.example.ui.network

import com.example.ui.network.dto.IssuanceDto
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

class IssuanceApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getIssuance(userId: UUID? = null, bookId: UUID? = null): List<IssuanceDto> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/issuance") {
            parameter("userId", userId)
            parameter("bookId", bookId)
        }
        return response.body()
    }

    suspend fun createIssuance(issuanceDto: IssuanceDto) {
        client.post("http://10.0.2.2:8080/issuance") {
            contentType(ContentType.Application.Json)
            setBody(issuanceDto)
        }
    }

    suspend fun updateIssuance(issuanceDto: IssuanceDto) {
        client.put("http://10.0.2.2:8080/issuance") {
            contentType(ContentType.Application.Json)
            setBody(issuanceDto)
        }
    }

    suspend fun deleteIssuance(id: UUID) {
        client.delete("http://10.0.2.2:8080/issuance/$id")
    }
}
