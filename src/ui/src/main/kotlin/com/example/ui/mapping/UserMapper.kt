package com.example.ui.mapping

import com.example.ui.model.UserModel
import com.example.ui.network.dto.UserDto
import javax.inject.Inject

class UserMapper @Inject constructor() {
    suspend fun toUi(user: UserDto) = UserModel(
        id = user.id,
        name = user.name,
        surname = user.surname,
        secondName = user.secondName,
        password = user.password,
        phoneNumber = user.phoneNumber,
        email = user.email,
        role = user.role,
    )

    suspend fun toDto(user: UserModel) = UserDto(
        id = user.id,
        name = user.name,
        surname = user.surname,
        secondName = user.secondName,
        password = user.password,
        phoneNumber = user.phoneNumber,
        email = user.email,
        role = user.role,
    )
}
