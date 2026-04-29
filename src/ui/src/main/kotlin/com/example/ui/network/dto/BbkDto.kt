package com.example.ui.network.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BbkDto(
    val id: @Contextual UUID = UUID.randomUUID(),
    val code: String,
    val description: String,
)
