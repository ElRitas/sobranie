package com.example.ui.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class IssuanceModel(
    val id: @Contextual UUID,
    val bookModel: BookModel,
    val userModel: UserModel,
    val issuanceDate: @Contextual LocalDate,
    val returnDate: @Contextual LocalDate,
)
