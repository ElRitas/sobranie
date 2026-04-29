package com.example.ui.screens.formScreen.mapping

import com.example.ui.model.BookModel
import com.example.ui.screens.formScreen.form.BookForm

object BookMapper {
    fun toModel(form: BookForm) = BookModel(
        id = form.id,
        title = form.title,
        annotation = form.annotation.ifEmpty { null },
        authors = form.authors,
        publisherModel = null,
        publicationYear = form.publicationYear.toIntOrNull(),
        codeISBN = form.codeISBN,
        bbkModel = form.bbk!!,
        mediaType = form.mediaType.ifEmpty { null },
        volume = form.volume.ifEmpty { null },
        language = form.language.ifEmpty { null },
        originalLanguage = form.originalLanguage.ifEmpty { null },
        copies = form.copies,
        availableCopies = form.availableCopies,
    )

    fun toForm(model: BookModel) = BookForm(
        id = model.id,
        title = model.title,
        annotation = model.annotation ?: "",
        authors = model.authors,
        publisher = model.publisherModel,
        publicationYear = if (model.publicationYear == null) "" else model.publicationYear.toString(),
        codeISBN = model.codeISBN ?: "",
        bbk = model.bbkModel,
        mediaType = model.mediaType ?: "",
        volume = model.volume ?: "",
        language = model.language ?: "",
        originalLanguage = model.language ?: "",
        copies = model.copies,
        availableCopies = model.copies
    )
}
