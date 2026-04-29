package com.example.ui.screens.formScreen.mapping

import com.example.ui.model.UserModel
import com.example.ui.screens.formScreen.form.UserForm

object UserMapper {
    fun toModel(form: UserForm) = UserModel(
        id = form.id,
        name = form.name,
        surname = form.surname,
        secondName = form.secondName.ifEmpty { null },
        password = form.password,
        phoneNumber = form.phoneNumber,
        email = form.email,
        role = form.role,
    )

    fun toForm(model: UserModel) = UserForm(
        id = model.id,
        name = model.name,
        surname = model.surname,
        secondName = model.secondName ?: "",
        password = model.password,
        phoneNumber = model.phoneNumber,
        email = model.email ?: "",
        role = model.role,
    )
}
