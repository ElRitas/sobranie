package com.example.ui.network

import com.example.ui.network.dto.ApuDto
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

class ApuApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getApu(id: UUID): ApuDto {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/apu/$id")
        return response.body()
    }

    suspend fun getApu(name: String): List<ApuDto> {
        val response: HttpResponse = client.get("http://10.0.2.2:8080/apu/by-term") {
            parameter("term", name)
        }
        return response.body()
    }

    suspend fun createApu(apuDto: ApuDto) {
        client.post("http://10.0.2.2:8080/apu") {
            contentType(ContentType.Application.Json)
            setBody(apuDto)
        }
    }

    suspend fun updateApu(apuDto: ApuDto) {
        client.put("http://10.0.2.2:8080/apu") {
            contentType(ContentType.Application.Json)
            setBody(apuDto)
        }
    }

    suspend fun deleteApu(id: UUID) {
        client.delete("http://10.0.2.2:8080/apu/$id")
    }
}
