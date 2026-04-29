package com.example.ui.screens.authorBooks

import com.example.ui.model.BookModel

sealed class AuthorBooksState {
    object Loading : AuthorBooksState()
    data class Success(val books: List<BookModel>) : AuthorBooksState()
    data class Error(val message: String) : AuthorBooksState()
}
