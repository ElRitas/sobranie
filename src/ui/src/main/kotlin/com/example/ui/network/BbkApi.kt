package com.example.ui.network

import com.example.ui.network.dto.BbkDto
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

class BbkApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getBbk(id: UUID): BbkDto {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/bbk/$id")
        return response.body()
    }

    suspend fun getBbk(code: String): List<BbkDto> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/bbk/by-code") {
            parameter("code", code)
        }
        return response.body()
    }

    suspend fun createBbk(bbkDto: BbkDto) {
        client.post("http://10.0.2.2:8080/bbk") {
            contentType(ContentType.Application.Json)
            setBody(bbkDto)
        }
    }

    suspend fun updateBbk(bbkDto: BbkDto) {
        client.put("http://10.0.2.2:8080/bbk") {
            contentType(ContentType.Application.Json)
            setBody(bbkDto)
        }
    }

    suspend fun deleteBbk(id: UUID) {
        client.delete("http://10.0.2.2:8080/bbk/$id")
    }
}
