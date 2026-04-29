package com.example.ui.screens.userReservation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.ReservationMapper
import com.example.ui.network.ReservationApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserReservationViewModel @Inject constructor(
    private val reservationApi: ReservationApi,
    private val mapper: ReservationMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow<UserReservationState>(UserReservationState.Loading)
    val state: StateFlow<UserReservationState> = _state

    private val userId = UUID.fromString(savedStateHandle.get<String>("userId")!!)

    fun loadBooks() {
        viewModelScope.launch {
            _state.value = UserReservationState.Loading
            try {
                val books = reservationApi.getReservation(userId = userId).map { mapper.toUi(it) }
                _state.value = UserReservationState.Success(books)
            } catch (e: Exception) {
                _state.value = UserReservationState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
