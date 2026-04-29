package com.example.ui.network.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class QueueDto(
    val id: @Contextual UUID = UUID.randomUUID(),
    val bookId: @Contextual UUID,
    val userId: @Contextual UUID,
    val createdAt: @Contextual Instant,
)
