package com.example.ui.screens.userFavorite

import com.example.ui.model.BookModel

sealed class UserFavoriteState {
    object Loading : UserFavoriteState()
    data class Success(val books: List<BookModel>) : UserFavoriteState()
    data class Error(val message: String) : UserFavoriteState()
}
