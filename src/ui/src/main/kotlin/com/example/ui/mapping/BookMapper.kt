package com.example.ui.mapping

import com.example.ui.model.BookModel
import com.example.ui.model.PublisherModel
import com.example.ui.network.AuthorApi
import com.example.ui.network.BbkApi
import com.example.ui.network.PublisherApi
import com.example.ui.network.dto.BookDto
import javax.inject.Inject

class BookMapper @Inject constructor(
    private val authorApi: AuthorApi,
    private val bbkApi: BbkApi,
    private val publisherApi: PublisherApi
) {
    suspend fun toUi(book: BookDto): BookModel {
        val authors =
            book.authors.map { authorId -> AuthorMapper().toUi(authorApi.getAuthor(authorId)) }
        val bbk = BbkMapper().toUi(bbkApi.getBbk(book.bbkId))
        var publisher: PublisherModel? = null
        if (book.publisherId != null) {
            publisher = PublisherMapper().toUi(publisherApi.getPublisher(book.publisherId))
        }

        return BookModel(
            id = book.id,
            title = book.title,
            annotation = book.annotation,
            authors = authors,
            publisherModel = publisher,
            publicationYear = book.publicationYear,
            codeISBN = book.codeISBN,
            bbkModel = bbk,
            mediaType = book.mediaType,
            volume = book.volume,
            language = book.language,
            originalLanguage = book.originalLanguage,
            copies = book.copies,
            availableCopies = book.availableCopies,
        )
    }

    suspend fun toDto(book: BookModel) = BookDto(
        id = book.id,
        title = book.title,
        annotation = book.annotation,
        authors = book.authors.map { it.id },
        publisherId = book.publisherModel?.id,
        publicationYear = book.publicationYear,
        codeISBN = book.codeISBN,
        bbkId = book.bbkModel.id,
        mediaType = book.mediaType,
        volume = book.volume,
        language = book.language,
        originalLanguage = book.originalLanguage,
        copies = book.copies,
        availableCopies = book.availableCopies,
    )
}
