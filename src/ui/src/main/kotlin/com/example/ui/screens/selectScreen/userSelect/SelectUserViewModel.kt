package com.example.ui.screens.selectScreen.userSelect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.UserMapper
import com.example.ui.model.UserModel
import com.example.ui.network.UserApi
import com.example.ui.screens.selectScreen.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectUserViewModel @Inject constructor(
    private val userApi: UserApi,
    private val mapper: UserMapper
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState<UserModel>>(SearchState.Empty)
    val state: StateFlow<SearchState<UserModel>> = _state

    fun search(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _state.value = SearchState.Empty
                return@launch
            }
            _state.value = SearchState.Loading
            try {
                val user = userApi.getUser(query).map { mapper.toUi(it) }
                _state.value = SearchState.Success(user)
            } catch (e: Exception) {
                _state.value = SearchState.Error(e.message ?: "Unknown error")
            }
        }
    }

}
