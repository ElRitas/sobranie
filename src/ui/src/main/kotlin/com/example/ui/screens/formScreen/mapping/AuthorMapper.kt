package com.example.ui.screens.formScreen.mapping

import com.example.ui.model.AuthorModel
import com.example.ui.screens.formScreen.form.AuthorForm

object AuthorMapper {
    fun toModel(form: AuthorForm) = AuthorModel(
        id = form.id,
        name = form.name
    )

    fun toForm(model: AuthorModel) = AuthorForm(
        id = model.id,
        name = model.name
    )
}
