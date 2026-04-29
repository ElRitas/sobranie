package com.example.ui.screens.userIssuance

import com.example.ui.model.IssuanceModel

sealed class UserIssuanceState {
    object Loading : UserIssuanceState()
    data class Success(val issuanceList: List<IssuanceModel>) : UserIssuanceState()
    data class Error(val message: String) : UserIssuanceState()
}
