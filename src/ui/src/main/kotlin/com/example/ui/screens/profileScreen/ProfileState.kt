package com.example.ui.screens.profileScreen

sealed class ProfileState {
    object Empty : ProfileState()
    object Loading : ProfileState()
    object Success : ProfileState()
    data class Error(val message: String) : ProfileState()
}
