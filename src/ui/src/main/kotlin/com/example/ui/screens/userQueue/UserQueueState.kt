package com.example.ui.screens.userQueue

import com.example.ui.model.QueueModel

sealed class UserQueueState {
    object Loading : UserQueueState()
    data class Success(val queueList: List<QueueModel>) : UserQueueState()
    data class Error(val message: String) : UserQueueState()
}
