package com.example.ui.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class QueueModel(
    val id: @Contextual UUID,
    val bookModel: BookModel,
    val userModel: UserModel,
    val createdAt: @Contextual Instant,
    val positionNum: Int
)
