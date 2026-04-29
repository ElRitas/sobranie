package com.example.ui.screens.bookList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.BookMapper
import com.example.ui.model.BookModel
import com.example.ui.network.BookApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookListViewModel @Inject constructor(
    private val bookApi: BookApi,
    private val mapper: BookMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<BookListState>(BookListState.Loading)
    val state: StateFlow<BookListState> = _state

    // query приходит в случае поиска
    private val query: String? = savedStateHandle.get<String>("query")

    private var currentPage = 1
    private val pageSize = 20
    private var isLoading = false
    private var isLastPage = false

    private val books = mutableListOf<BookModel>()

    init {
        if (query == null) {
            loadBooksPage() // обычный список
        } else {
            searchBooks(query) // поиск
        }
    }

    fun loadBooksPage() {
        if (isLoading || isLastPage || query != null) return

        isLoading = true
        viewModelScope.launch {
            try {
                val newBooks = bookApi.getBooks(page = currentPage, pageSize = pageSize)
                    .map { mapper.toUi(it) }

                if (newBooks.isEmpty()) {
                    isLastPage = true
                } else {
                    books.addAll(newBooks)
                    _state.value = BookListState.Success(books.toList(), canLoadMore = !isLastPage)
                    currentPage++
                }
            } catch (e: Exception) {
                _state.value = BookListState.Error(e.message ?: "Unknown error")
            } finally {
                isLoading = false
            }
        }
    }

    private fun searchBooks(sentence: String) {
        viewModelScope.launch {
            _state.value = BookListState.Loading
            try {
                val books = bookApi.getBooksBySentence(sentence).map { mapper.toUi(it) }
                _state.value = BookListState.Success(books, canLoadMore = false)
            } catch (e: Exception) {
                _state.value = BookListState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
