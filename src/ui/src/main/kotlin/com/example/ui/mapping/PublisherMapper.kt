package com.example.ui.mapping

import com.example.ui.model.PublisherModel
import com.example.ui.network.dto.PublisherDto
import javax.inject.Inject

class PublisherMapper @Inject constructor() {
    suspend fun toUi(publisher: PublisherDto) = PublisherModel(
        id = publisher.id,
        name = publisher.name,
        description = publisher.description,
        email = publisher.email,
        phoneNumber = publisher.phoneNumber,
    )

    suspend fun toDto(publisher: PublisherModel) = PublisherDto(
        id = publisher.id,
        name = publisher.name,
        description = publisher.description,
        email = publisher.email,
        phoneNumber = publisher.phoneNumber,
    )
}
