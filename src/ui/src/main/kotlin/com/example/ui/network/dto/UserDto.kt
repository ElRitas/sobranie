package com.example.ui.network.dto

import com.example.ui.common.enums.UserRole
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserDto(
    val id: @Contextual UUID = UUID.randomUUID(),
    val name: String,
    val surname: String,
    val secondName: String? = null,
    val password: String,
    val email: String? = null,
    val phoneNumber: String,
    val role: UserRole,
)
