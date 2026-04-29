package com.example.ui.screens.formScreen.editEntity

sealed class EditEntityState {
    object Empty : EditEntityState()
    object Loading : EditEntityState()
    object Success : EditEntityState()
    data class Error(val message: String) : EditEntityState()
}
