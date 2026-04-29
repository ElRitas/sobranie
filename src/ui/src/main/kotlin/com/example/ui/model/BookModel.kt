package com.example.ui.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BookModel(
    val id: @Contextual UUID,
    val title: String,
    val annotation: String?,
    val authors: List<AuthorModel>,
    val publisherModel: PublisherModel?,
    val publicationYear: Int?,
    val codeISBN: String?,
    val bbkModel: BbkModel,
    val mediaType: String?,
    val volume: String?,
    val language: String?,
    val originalLanguage: String?,
    val copies: Int,
    val availableCopies: Int,
)
