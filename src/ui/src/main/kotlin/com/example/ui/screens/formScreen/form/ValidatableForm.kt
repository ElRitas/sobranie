package com.example.ui.screens.formScreen.form

interface ValidatableForm {
    fun validate(): Map<String, String?> // ключ — поле, значение — ошибка (null если ошибки нет)

    fun isValid(): Boolean = validate().values.all { it == null }
}
