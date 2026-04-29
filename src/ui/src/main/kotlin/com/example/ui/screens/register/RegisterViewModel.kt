package com.example.ui.screens.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.UserMapper
import com.example.ui.network.UserApi
import com.example.ui.screens.formScreen.form.UserForm
import com.example.ui.util.UserStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userApi: UserApi,
    private val mapper: UserMapper
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    var userForm by mutableStateOf(UserForm())

    fun registerUser(onComplete: () -> Unit) {
        viewModelScope.launch {
            _state.value = RegisterState.Loading
            try {
                val userModel =
                    com.example.ui.screens.formScreen.mapping.UserMapper.toModel(userForm)
                userApi.createUser(mapper.toDto(userModel))
                _state.value = RegisterState.Success("Регистрация прошла успешно")
                UserStore.save(userModel)
                onComplete()
            } catch (e: Exception) {
                _state.value = RegisterState.Error(e.message ?: "Ошибка регистрации")
            }
        }
    }
}
