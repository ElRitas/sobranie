package com.example.ui.mapping

import com.example.ui.model.ReservationModel
import com.example.ui.network.BookApi
import com.example.ui.network.UserApi
import com.example.ui.network.dto.ReservationDto
import javax.inject.Inject

class ReservationMapper @Inject constructor(
    val bookApi: BookApi,
    val userApi: UserApi,
    val bookMapper: BookMapper
) {
    suspend fun toUi(reservation: ReservationDto): ReservationModel {
        val bookModel = bookMapper.toUi(bookApi.getBook(reservation.bookId))
        val userModel = UserMapper().toUi(userApi.getUser(reservation.userId))
        return ReservationModel(
            id = reservation.id,
            bookModel = bookModel,
            userModel = userModel,
            reservationDate = reservation.reservationDate,
            cancelDate = reservation.cancelDate,
        )
    }

    suspend fun toDto(reservation: ReservationModel) = ReservationDto(
        id = reservation.id,
        bookId = reservation.bookModel.id,
        userId = reservation.userModel.id,
        reservationDate = reservation.reservationDate,
        cancelDate = reservation.cancelDate,
    )
}
