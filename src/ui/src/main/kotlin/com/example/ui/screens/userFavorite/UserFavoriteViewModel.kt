package com.example.ui.screens.userFavorite

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.BookMapper
import com.example.ui.network.UserApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserFavoriteViewModel @Inject constructor(
    private val userApi: UserApi,
    private val mapper: BookMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow<UserFavoriteState>(UserFavoriteState.Loading)
    val state: StateFlow<UserFavoriteState> = _state

    private val userId = UUID.fromString(savedStateHandle.get<String>("userId")!!)

    fun loadBooks() {
        viewModelScope.launch {
            _state.value = UserFavoriteState.Loading
            try {
                val books = userApi.getFavorite(userId).map { mapper.toUi(it) }
                _state.value = UserFavoriteState.Success(books)
            } catch (e: Exception) {
                _state.value = UserFavoriteState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
