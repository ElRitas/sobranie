package com.example.ui.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ApuModel(
    val id: @Contextual UUID,
    val term: String,
    val bbkModel: BbkModel,
)
