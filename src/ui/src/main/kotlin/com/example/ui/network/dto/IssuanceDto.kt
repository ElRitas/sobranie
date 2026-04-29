package com.example.ui.network.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class IssuanceDto(
    val id: @Contextual UUID = UUID.randomUUID(),
    val bookId: @Contextual UUID,
    val userId: @Contextual UUID,
    val issuanceDate: @Contextual LocalDate,
    val returnDate: @Contextual LocalDate,
)
