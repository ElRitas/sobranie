package com.example.ui.screens.selectScreen.bbkSelect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.BbkMapper
import com.example.ui.model.BbkModel
import com.example.ui.network.BbkApi
import com.example.ui.screens.selectScreen.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectBbkViewModel @Inject constructor(
    private val bbkApi: BbkApi,
    private val mapper: BbkMapper
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState<BbkModel>>(SearchState.Empty)
    val state: StateFlow<SearchState<BbkModel>> = _state

    fun search(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _state.value = SearchState.Empty
                return@launch
            }
            _state.value = SearchState.Loading
            try {
                val bbkList = bbkApi.getBbk(query).map { mapper.toUi(it) }
                _state.value = SearchState.Success(bbkList)
            } catch (e: Exception) {
                _state.value = SearchState.Error(e.message ?: "Unknown error")
            }
        }
    }

}
