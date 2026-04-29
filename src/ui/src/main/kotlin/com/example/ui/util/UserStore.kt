package com.example.ui.util


import com.example.ui.common.enums.UserRole
import com.example.ui.model.UserModel
import java.util.UUID

object UserStore {
    private var userId: UUID? = null
    private var role: UserRole? = null

    fun save(userModel: UserModel) {
        userId = userModel.id
        role = userModel.role
    }

    fun clear() {
        userId = null
        role = null
    }

    fun getId() = userId
    fun getRole() = role
}
