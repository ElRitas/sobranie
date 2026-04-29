package com.example.ui.screens.formScreen.mapping

import com.example.ui.model.PublisherModel
import com.example.ui.screens.formScreen.form.PublisherForm

object PublisherMapper {
    fun toModel(form: PublisherForm) = PublisherModel(
        id = form.id,
        name = form.name,
        description = form.description.ifEmpty { null },
        email = form.email.ifEmpty { null },
        phoneNumber = form.phoneNumber.ifEmpty { null },
    )

    fun toForm(model: PublisherModel) = PublisherForm(
        id = model.id,
        name = model.name,
        description = model.description ?: "",
        email = model.email ?: "",
        phoneNumber = if (model.phoneNumber == null) "" else model.phoneNumber.toString(),
    )
}
