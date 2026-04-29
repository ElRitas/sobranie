package com.example.ui.screens.userQueue

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.QueueMapper
import com.example.ui.network.QueueApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserQueueViewModel @Inject constructor(
    private val queueApi: QueueApi,
    private val mapper: QueueMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow<UserQueueState>(UserQueueState.Loading)
    val state: StateFlow<UserQueueState> = _state

    private val userId = UUID.fromString(savedStateHandle.get<String>("userId")!!)

    fun loadBooks() {
        viewModelScope.launch {
            _state.value = UserQueueState.Loading
            try {
                val books = queueApi.getQueue(userId = userId).map { mapper.toUi(it) }
                _state.value = UserQueueState.Success(books)
            } catch (e: Exception) {
                _state.value = UserQueueState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
