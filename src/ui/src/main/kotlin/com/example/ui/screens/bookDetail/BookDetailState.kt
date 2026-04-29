package com.example.ui.screens.bookDetail

import com.example.ui.model.BookModel

data class BookActionsState(
    val isFavorite: Boolean = false,
    val isReserved: Boolean = false,
    val isIssuance: Boolean = false,
    val queueNumber: Int = 0
)

sealed class BookDetailState {
    object Loading : BookDetailState()
    data class Success(val book: BookModel, val actionsState: BookActionsState) : BookDetailState()
    data class Error(val message: String) : BookDetailState()
}
