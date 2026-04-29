package com.example.ui.network.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ApuDto(
    val id: @Contextual UUID = UUID.randomUUID(),
    val term: String,
    val bbkId: @Contextual UUID
)
