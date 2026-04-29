package com.example.ui.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BbkModel(
    val id: @Contextual UUID,
    val code: String,
    val description: String,
)
