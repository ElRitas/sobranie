package com.example.ui.screens.userReservation

import com.example.ui.model.ReservationModel

sealed class UserReservationState {
    object Loading : UserReservationState()
    data class Success(val reservations: List<ReservationModel>) : UserReservationState()
    data class Error(val message: String) : UserReservationState()
}
