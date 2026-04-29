package com.example.ui.screens.bbkBooks

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
class BbkBooksViewModel @Inject constructor(
    private val bookApi: BookApi,
    private val mapper: BookMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow<BbkBooksState>(BbkBooksState.Loading)
    val state: StateFlow<BbkBooksState> = _state

    private val bbkId = UUID.fromString(savedStateHandle.get<String>("bbkId"))

    fun loadBooks() {
        viewModelScope.launch {
            _state.value = BbkBooksState.Loading
            try {
                val books = bookApi.getBooksByBbkId(bbkId).map { mapper.toUi(it) }
                _state.value = BbkBooksState.Success(books)
            } catch (e: Exception) {
                _state.value = BbkBooksState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
