package com.example.ui.screens.selectScreen.authorSelect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.AuthorMapper
import com.example.ui.model.AuthorModel
import com.example.ui.network.AuthorApi
import com.example.ui.screens.selectScreen.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectAuthorViewModel @Inject constructor(
    private val authorApi: AuthorApi,
    private val mapper: AuthorMapper
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState<AuthorModel>>(SearchState.Empty)
    val state: StateFlow<SearchState<AuthorModel>> = _state

    fun search(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _state.value = SearchState.Empty
                return@launch
            }
            _state.value = SearchState.Loading
            try {
                val authors = authorApi.getAuthor(query).map { mapper.toUi(it) }
                _state.value = SearchState.Success(authors)
            } catch (e: Exception) {
                _state.value = SearchState.Error(e.message ?: "Unknown error")
            }
        }
    }

}
