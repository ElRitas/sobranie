package com.example.ui.screens.profileScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.UserMapper
import com.example.ui.network.UserApi
import com.example.ui.screens.formScreen.form.UserForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userApi: UserApi,
    private val userMapper: UserMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Empty)
    val state: StateFlow<ProfileState> = _state

    private val authorId = UUID.fromString(savedStateHandle.get<String>("userId"))

    var userForm by mutableStateOf(UserForm())

    fun loadUser() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            try {
                val userModel = userMapper.toUi(userApi.getUser(authorId))
                userForm = com.example.ui.screens.formScreen.mapping.UserMapper.toForm(userModel)
                _state.value = ProfileState.Empty
            } catch (e: Exception) {
                _state.value = ProfileState.Error(e.message.toString())
            }
        }
    }

    fun updateUser() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            try {
                if (userForm.isValid()) {
                    val model =
                        com.example.ui.screens.formScreen.mapping.UserMapper.toModel(userForm)
                    userApi.updateUser(userMapper.toDto(model))
                    _state.value = ProfileState.Success
                } else {
                    _state.value = ProfileState.Error("Форма содержит ошибки")
                }
            } catch (e: Exception) {
                _state.value = ProfileState.Error(e.message.toString())
            }
        }
    }
}
