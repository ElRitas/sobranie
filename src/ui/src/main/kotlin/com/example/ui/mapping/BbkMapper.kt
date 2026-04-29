package com.example.ui.mapping

import com.example.ui.model.BbkModel
import com.example.ui.network.dto.BbkDto
import javax.inject.Inject

class BbkMapper @Inject constructor() {
    suspend fun toUi(bbk: BbkDto) = BbkModel(
        id = bbk.id,
        code = bbk.code,
        description = bbk.description,
    )

    suspend fun toDto(bbk: BbkModel) = BbkDto(
        id = bbk.id,
        code = bbk.code,
        description = bbk.description,
    )
}
