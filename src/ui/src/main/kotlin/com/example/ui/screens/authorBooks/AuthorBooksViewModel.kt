package com.example.ui.screens.authorBooks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.BookMapper
import com.example.ui.network.BookApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AuthorBooksViewModel @Inject constructor(
    private val bookApi: BookApi,
    private val mapper: BookMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow<AuthorBooksState>(AuthorBooksState.Loading)
    val state: StateFlow<AuthorBooksState> = _state

    private val authorId = UUID.fromString(savedStateHandle.get<String>("authorId"))

    fun loadBooks() {
        viewModelScope.launch {
            _state.value = AuthorBooksState.Loading
            try {
                val books = bookApi.getBooksByAuthorId(authorId).map { mapper.toUi(it) }
                _state.value = AuthorBooksState.Success(books)
            } catch (e: Exception) {
                _state.value = AuthorBooksState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
