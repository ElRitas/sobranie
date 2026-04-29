package com.example.ui.screens.bookReservation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.IssuanceMapper
import com.example.ui.mapping.ReservationMapper
import com.example.ui.model.IssuanceModel
import com.example.ui.model.ReservationModel
import com.example.ui.network.IssuanceApi
import com.example.ui.network.ReservationApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class BookReservationViewModel @Inject constructor(
    private val reservationApi: ReservationApi,
    private val issuanceApi: IssuanceApi,
    private val reservationMapper: ReservationMapper,
    private val issuanceMapper: IssuanceMapper,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val bookId: UUID = UUID.fromString(savedStateHandle["bookId"]!!)

    private val _uiState = MutableStateFlow<BookReservationState>(BookReservationState.Loading)
    val uiState: StateFlow<BookReservationState> = _uiState.asStateFlow()

    init {
        loadReservations()
    }

    fun loadReservations() {
        viewModelScope.launch {
            try {
                val reservations =
                    reservationApi.getReservation(bookId = bookId)
                        .map { reservationMapper.toUi(it) }
                _uiState.value = BookReservationState.Success(reservations)
            } catch (e: Exception) {
                _uiState.value = BookReservationState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

    fun approveReservation(reservationModel: ReservationModel) {
        viewModelScope.launch {
            try {
                val issuanceModel = IssuanceModel(
                    id = reservationModel.id,
                    bookModel = reservationModel.bookModel,
                    userModel = reservationModel.userModel,
                    issuanceDate = LocalDate.now(),
                    returnDate = LocalDate.now().plusWeeks(3)
                )
                issuanceApi.createIssuance(issuanceMapper.toDto(issuanceModel))
                loadReservations()
            } catch (e: Exception) {
                _uiState.value = BookReservationState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

    fun cancelReservation(reservationId: UUID) {
        viewModelScope.launch {
            try {
                reservationApi.deleteReservation(reservationId)
                loadReservations()
            } catch (e: Exception) {
                _uiState.value = BookReservationState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

}
