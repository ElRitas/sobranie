package com.example.ui.mapping

import com.example.ui.model.AuthorModel
import com.example.ui.network.dto.AuthorDto
import javax.inject.Inject

class AuthorMapper @Inject constructor() {
    suspend fun toUi(authorDto: AuthorDto) = AuthorModel(
        id = authorDto.id,
        name = authorDto.name,
    )

    suspend fun toDto(author: AuthorModel) = AuthorDto(
        id = author.id,
        name = author.name,
    )
}
