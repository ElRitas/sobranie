package com.example.ui.mapping

import com.example.ui.model.QueueModel
import com.example.ui.network.BookApi
import com.example.ui.network.QueueApi
import com.example.ui.network.UserApi
import com.example.ui.network.dto.QueueDto
import javax.inject.Inject

class QueueMapper @Inject constructor(
    val bookApi: BookApi,
    val userApi: UserApi,
    val queueApi: QueueApi,
    val bookMapper: BookMapper
) {
    suspend fun toUi(queue: QueueDto): QueueModel {
        val bookModel = bookMapper.toUi(bookApi.getBook(queue.bookId))
        val userModel = UserMapper().toUi(userApi.getUser(queue.userId))
        return QueueModel(
            id = queue.id,
            bookModel = bookModel,
            userModel = userModel,
            createdAt = queue.createdAt,
            positionNum = queueApi.getQueueNumber(queue.bookId, queue.userId)!!
        )
    }

    suspend fun toDto(queue: QueueModel) = QueueDto(
        id = queue.id,
        bookId = queue.bookModel.id,
        userId = queue.userModel.id,
        createdAt = queue.createdAt,
    )
}
