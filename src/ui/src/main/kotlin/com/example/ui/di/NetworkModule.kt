package com.example.ui.di

import com.example.ui.common.serializer.InstantSerializer
import com.example.ui.common.serializer.LocalDateSerializer
import com.example.ui.common.serializer.UUIDSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            expectSuccess = true
            install(ContentNegotiation) {
                json(Json {
                    serializersModule = SerializersModule {
                        contextual(UUID::class, UUIDSerializer)
                        contextual(LocalDate::class, LocalDateSerializer)
                        contextual(Instant::class, InstantSerializer)
                    }
                })
            }
        }
    }
}
