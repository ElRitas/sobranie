package com.example.ui.screens.selectScreen.apuSelect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.ApuMapper
import com.example.ui.model.ApuModel
import com.example.ui.network.ApuApi
import com.example.ui.screens.selectScreen.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectApuViewModel @Inject constructor(
    private val apuApi: ApuApi,
    private val mapper: ApuMapper
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState<ApuModel>>(SearchState.Empty)
    val state: StateFlow<SearchState<ApuModel>> = _state

    fun search(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _state.value = SearchState.Empty
                return@launch
            }
            _state.value = SearchState.Loading
            try {
                val authors = apuApi.getApu(query).map { mapper.toUi(it) }
                _state.value = SearchState.Success(authors)
            } catch (e: Exception) {
                _state.value = SearchState.Error(e.message ?: "Unknown error")
            }
        }
    }

}
