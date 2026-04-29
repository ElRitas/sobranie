package com.example.ui.screens.userScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.UserMapper
import com.example.ui.network.UserApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userApi: UserApi,
    private val mapper: UserMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserState>(UserState.Loading)
    val uiState: StateFlow<UserState> = _uiState.asStateFlow()

    private val userId = UUID.fromString(savedStateHandle.get<String>("userId")!!)

    fun loadUser() {
        viewModelScope.launch {
            try {
                val user = mapper.toUi(userApi.getUser(userId))
                _uiState.value = UserState.Success(user)
            } catch (e: Exception) {
                _uiState.value = UserState.Error(e.message ?: "Не удалось загрузить пользователя")
            }
        }
    }
}
