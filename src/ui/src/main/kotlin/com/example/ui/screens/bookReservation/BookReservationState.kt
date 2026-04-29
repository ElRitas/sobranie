package com.example.ui.screens.bookReservation

import com.example.ui.model.ReservationModel


sealed class BookReservationState {
    object Loading : BookReservationState()
    data class Success(val reservationList: List<ReservationModel>) : BookReservationState()
    data class Error(val message: String) : BookReservationState()
}
