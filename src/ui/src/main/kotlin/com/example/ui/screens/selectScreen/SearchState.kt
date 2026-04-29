package com.example.ui.screens.selectScreen

sealed class SearchState<out T> {
    object Empty : SearchState<Nothing>()
    object Loading : SearchState<Nothing>()
    data class Success<T>(val results: List<T>) : SearchState<T>()
    data class Error(val message: String) : SearchState<Nothing>()
}
