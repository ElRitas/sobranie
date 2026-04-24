package com.example.ui.common.json

import com.example.ui.common.serializer.InstantSerializer
import com.example.ui.common.serializer.LocalDateSerializer
import com.example.ui.common.serializer.UUIDSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

val appJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    prettyPrint = false
    isLenient = true
    serializersModule = SerializersModule {
        contextual(UUID::class, UUIDSerializer)
        contextual(LocalDate::class, LocalDateSerializer)
        contextual(Instant::class, InstantSerializer)
    }
}
