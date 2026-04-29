package com.example.ui.screens.selectScreen.bookSelect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.BookMapper
import com.example.ui.model.BookModel
import com.example.ui.network.BookApi
import com.example.ui.screens.selectScreen.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectBookViewModel @Inject constructor(
    private val bookApi: BookApi,
    private val mapper: BookMapper
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState<BookModel>>(SearchState.Empty)
    val state: StateFlow<SearchState<BookModel>> = _state

    fun search(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _state.value = SearchState.Empty
                return@launch
            }
            _state.value = SearchState.Loading
            try {
                val books = bookApi.getBooksBySentence(query).map { mapper.toUi(it) }
                _state.value = SearchState.Success(books)
            } catch (e: Exception) {
                _state.value = SearchState.Error(e.message ?: "Unknown error")
            }
        }
    }

}
