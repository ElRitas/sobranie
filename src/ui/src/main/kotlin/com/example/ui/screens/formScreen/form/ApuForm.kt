package com.example.ui.screens.formScreen.form

import com.example.ui.model.BbkModel
import java.util.UUID

data class ApuForm(
    val id: UUID = UUID.randomUUID(),
    val term: String = "",
    val bbk: BbkModel? = null
) : ValidatableForm {
    override fun validate(): Map<String, String?> {
        val errors = mutableMapOf<String, String?>()
        errors["term"] = if (term.isBlank()) "Ключевое слово обязательно" else null
        errors["bbk"] = if (bbk == null) "ББК-код обязателен" else null

        return errors
    }
}
