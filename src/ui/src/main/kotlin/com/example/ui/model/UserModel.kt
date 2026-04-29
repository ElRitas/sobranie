package com.example.ui.model

import com.example.ui.common.enums.UserRole
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserModel(
    val id: @Contextual UUID,
    val name: String,
    val surname: String,
    val secondName: String?,
    val password: String,
    val email: String?,
    val phoneNumber: String,
    val role: UserRole,
) {
    fun getFullName(): String {
        return "$surname $name ${(secondName ?: "")}"
    }
}
