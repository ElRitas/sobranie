package com.example.ui.screens.bookDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.BookMapper
import com.example.ui.model.BookModel
import com.example.ui.network.BookApi
import com.example.ui.network.QueueApi
import com.example.ui.network.ReservationApi
import com.example.ui.network.UserApi
import com.example.ui.network.dto.QueueDto
import com.example.ui.network.dto.ReservationDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val userApi: UserApi,
    private val reservationApi: ReservationApi,
    private val queueApi: QueueApi,
) : ViewModel() {
    private val _state = MutableStateFlow<BookDetailState>(BookDetailState.Loading)
    val state: StateFlow<BookDetailState> = _state

    fun setBook(book: BookModel, userId: UUID?) {
        viewModelScope.launch {
            if (userId == null) {
                _state.value = BookDetailState.Success(book, BookActionsState())
            } else {
                val bookActionsState = BookActionsState(
                    isFavorite = checkIfFavorite(userId, book.id),
                    isReserved = checkIfReserved(userId, book.id),
                    queueNumber = getQueuePosition(userId, book.id)
                )
                _state.value = BookDetailState.Success(book, bookActionsState)
            }
        }
    }

    private suspend fun getQueuePosition(userId: UUID, bookId: UUID): Int {
        return queueApi.getQueueNumber(bookId, userId)
    }

    fun toggleFavorite(userId: UUID, bookId: UUID) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is BookDetailState.Success) {
                val current = currentState.actionsState
                if (current.isFavorite) {
                    userApi.removeFromFavorite(userId, bookId)
                } else {
                    userApi.addToFavorite(userId, bookId)
                }

                _state.value = currentState.copy(
                    actionsState = currentState.actionsState.copy(isFavorite = !current.isFavorite)
                )
            }
        }
    }

    fun toggleReservation(userId: UUID, bookId: UUID) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is BookDetailState.Success) {
                val current = currentState.actionsState
                if (current.isReserved) {
                    deleteReservation(userId, bookId)
                } else {
                    createReservation(userId, bookId)
                }

                _state.value = currentState.copy(
                    actionsState = currentState.actionsState.copy(isReserved = !current.isReserved)
                )
            }
        }
    }

    fun toggleQueue(userId: UUID, bookId: UUID) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is BookDetailState.Success) {
                val current = currentState.actionsState
                if (current.queueNumber == 0) {
                    enqueue(userId, bookId)
                    _state.value = currentState.copy(
                        actionsState = currentState.actionsState.copy(
                            queueNumber = getQueuePosition(
                                userId,
                                bookId
                            )
                        )
                    )
                } else {
                    dequeue(userId, bookId)
                    _state.value = currentState.copy(
                        actionsState = currentState.actionsState.copy(queueNumber = 0)
                    )
                }
            }
        }
    }

    private suspend fun createReservation(userId: UUID, bookId: UUID) {
        val newReservation = ReservationDto(
            id = UUID.randomUUID(),
            bookId = bookId,
            userId = userId,
            reservationDate = LocalDate.now(),
            cancelDate = LocalDate.now().plusDays(3)
        )
        reservationApi.createReservation(newReservation)
    }

    private suspend fun deleteReservation(userId: UUID, bookId: UUID) {
        val reservation = reservationApi.getReservation(userId, bookId).first()
        reservationApi.deleteReservation(reservation.id)
    }

    private suspend fun checkIfFavorite(userId: UUID, bookId: UUID): Boolean {
        val books = userApi.getFavoriteById(userId)
        return books.any { it.id == bookId }
    }

    private suspend fun checkIfReserved(userId: UUID, bookId: UUID): Boolean {
        val books = reservationApi.getReservation(userId, bookId)
        return books.any { it.bookId == bookId }
    }

    private suspend fun enqueue(userId: UUID, bookId: UUID) {
        val newQueue = QueueDto(
            id = UUID.randomUUID(),
            bookId = bookId,
            userId = userId,
            createdAt = Instant.now()
        )
        queueApi.createQueue(newQueue)
    }

    private suspend fun dequeue(userId: UUID, bookId: UUID) {
        val item = queueApi.getQueue(userId, bookId).first()
        queueApi.deleteQueue(item.id)
    }

}
