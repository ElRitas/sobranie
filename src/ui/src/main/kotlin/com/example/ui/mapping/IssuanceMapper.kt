package com.example.ui.mapping

import com.example.ui.model.IssuanceModel
import com.example.ui.network.BookApi
import com.example.ui.network.UserApi
import com.example.ui.network.dto.IssuanceDto
import javax.inject.Inject

class IssuanceMapper @Inject constructor(
    val bookApi: BookApi,
    val userApi: UserApi,
    val bookMapper: BookMapper
) {
    suspend fun toUi(issuance: IssuanceDto): IssuanceModel {
        val bookModel = bookMapper.toUi(bookApi.getBook(issuance.bookId))
        val userModel = UserMapper().toUi(userApi.getUser(issuance.userId))
        return IssuanceModel(
            id = issuance.id,
            bookModel = bookModel,
            userModel = userModel,
            issuanceDate = issuance.issuanceDate,
            returnDate = issuance.returnDate,
        )
    }

    suspend fun toDto(issuance: IssuanceModel) = IssuanceDto(
        id = issuance.id,
        bookId = issuance.bookModel.id,
        userId = issuance.userModel.id,
        issuanceDate = issuance.issuanceDate,
        returnDate = issuance.returnDate,
    )
}
