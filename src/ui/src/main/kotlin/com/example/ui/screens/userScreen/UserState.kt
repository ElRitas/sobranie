package com.example.ui.screens.userScreen

import com.example.ui.model.UserModel

sealed class UserState {
    object Loading : UserState()
    data class Success(val user: UserModel) : UserState()
    data class Error(val message: String) : UserState()
}
