package com.example.ui.screens.formScreen.form

import java.util.UUID

data class PublisherForm(
    val id: UUID = UUID.randomUUID(),
    val name: String = "",
    val description: String = "",
    val email: String = "",
    val phoneNumber: String = "",
) : ValidatableForm {
    override fun validate(): Map<String, String?> {
        val errors = mutableMapOf<String, String?>()
        errors["name"] = if (name.isBlank()) "Название обязательно" else null

        errors["phoneNumber"] = checkPhone(phoneNumber)
        errors["email"] = checkEmail(email)

        return errors
    }

    fun checkPhone(phoneNumber: String): String? {
        if (phoneNumber.isBlank()) return "Номер телефона обязателен"
        val phoneRegex = "^\\+?[78][0-9]{10}$".toRegex()
        if (!phoneNumber.matches(phoneRegex)) return "Некорректный номер телефона"
        return null
    }

    fun checkEmail(email: String): String? {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        if (email.isNotBlank() && !email.matches(emailRegex)) {
            return "Неверный формат электронной почты"
        }
        return null
    }

}
