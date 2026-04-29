package com.example.ui.screens.bookIssuance

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.IssuanceMapper
import com.example.ui.network.IssuanceApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class BookIssuanceViewModel @Inject constructor(
    private val issuanceApi: IssuanceApi,
    private val mapper: IssuanceMapper,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val bookId: UUID = UUID.fromString(savedStateHandle["bookId"]!!)

    private val _uiState = MutableStateFlow<BookIssuanceState>(BookIssuanceState.Loading)
    val uiState: StateFlow<BookIssuanceState> = _uiState.asStateFlow()

    init {
        loadReservations()
    }

    fun loadReservations() {
        viewModelScope.launch {
            try {
                val issuanceList =
                    issuanceApi.getIssuance(bookId = bookId).map { mapper.toUi(it) }
                _uiState.value = BookIssuanceState.Success(issuanceList)
            } catch (e: Exception) {
                _uiState.value = BookIssuanceState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

    fun returnBook(issuanceId: UUID) {
        viewModelScope.launch {
            try {
                issuanceApi.deleteIssuance(issuanceId)
                loadReservations()
            } catch (e: Exception) {
                _uiState.value = BookIssuanceState.Error(e.message ?: "Ошибка загрузки")
            }
        }
    }

}
