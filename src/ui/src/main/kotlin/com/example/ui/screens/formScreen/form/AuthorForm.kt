package com.example.ui.screens.formScreen.form

import java.util.UUID

data class AuthorForm(
    val id: UUID = UUID.randomUUID(),
    val name: String = ""
) : ValidatableForm {
    override fun validate(): Map<String, String?> {
        val errors = mutableMapOf<String, String?>()
        errors["name"] = if (name.isBlank()) "Имя автора обязательно" else null

        return errors
    }
}
