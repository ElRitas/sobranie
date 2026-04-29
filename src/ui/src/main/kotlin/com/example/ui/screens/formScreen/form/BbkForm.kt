package com.example.ui.screens.formScreen.form

import java.util.UUID


data class BbkForm(
    val id: UUID = UUID.randomUUID(),
    val code: String = "",
    val description: String = ""
) : ValidatableForm {
    override fun validate(): Map<String, String?> {
        val errors = mutableMapOf<String, String?>()
        errors["code"] = if (code.isBlank()) "ББК-код обязательно" else null
        errors["description"] =
            if (description.isBlank()) "Описание обязательно обязательно" else null

        return errors
    }
}
