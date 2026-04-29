package com.example.ui.screens.formScreen.form

import com.example.ui.model.AuthorModel
import com.example.ui.model.BbkModel
import com.example.ui.model.PublisherModel
import java.util.UUID

data class BookForm(
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val annotation: String = "",
    val authors: List<AuthorModel> = emptyList(),
    val publisher: PublisherModel? = null,
    val publicationYear: String = "",
    val codeISBN: String = "",
    val bbk: BbkModel? = null,
    val mediaType: String = "",
    val volume: String = "",
    val language: String = "",
    val originalLanguage: String = "",
    val copies: Int = 1,
    val availableCopies: Int = 1
) : ValidatableForm {
    override fun validate(): Map<String, String?> {
        val errors = mutableMapOf<String, String?>()
        errors["title"] = if (title.isBlank()) "Название обязательно" else null
        errors["bbk"] = if (bbk == null) "ББК-код обязателен" else null
        errors["copies"] = if (copies < 0) "Количество копий не может быть меньше нуля" else null

        return errors
    }
}
