package com.example.ui.screens.selectScreen.publisherSelect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.PublisherMapper
import com.example.ui.model.PublisherModel
import com.example.ui.network.PublisherApi
import com.example.ui.screens.selectScreen.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectPublisherViewModel @Inject constructor(
    private val publisherApi: PublisherApi,
    private val mapper: PublisherMapper
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState<PublisherModel>>(SearchState.Empty)
    val state: StateFlow<SearchState<PublisherModel>> = _state

    fun search(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _state.value = SearchState.Empty
                return@launch
            }
            _state.value = SearchState.Loading
            try {
                val authors = publisherApi.getPublisher(query).map { mapper.toUi(it) }
                _state.value = SearchState.Success(authors)
            } catch (e: Exception) {
                _state.value = SearchState.Error(e.message ?: "Unknown error")
            }
        }
    }

}
