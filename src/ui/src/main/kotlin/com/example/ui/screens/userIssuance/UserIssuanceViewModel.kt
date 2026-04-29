package com.example.ui.screens.userIssuance

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.IssuanceMapper
import com.example.ui.network.IssuanceApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserIssuanceViewModel @Inject constructor(
    private val issuanceApi: IssuanceApi,
    private val mapper: IssuanceMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow<UserIssuanceState>(UserIssuanceState.Loading)
    val state: StateFlow<UserIssuanceState> = _state

    private val userId = UUID.fromString(savedStateHandle.get<String>("userId")!!)

    fun loadBooks() {
        viewModelScope.launch {
            _state.value = UserIssuanceState.Loading
            try {
                val books = issuanceApi.getIssuance(userId = userId).map { mapper.toUi(it) }
                _state.value = UserIssuanceState.Success(books)
            } catch (e: Exception) {
                _state.value = UserIssuanceState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
