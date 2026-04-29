package com.example.ui.mapping

import com.example.ui.model.ApuModel
import com.example.ui.network.BbkApi
import com.example.ui.network.dto.ApuDto
import javax.inject.Inject

class ApuMapper @Inject constructor(
    private val bbkApi: BbkApi
) {
    suspend fun toUi(apu: ApuDto): ApuModel {
        val bbk = BbkMapper().toUi(bbkApi.getBbk(apu.bbkId))
        return ApuModel(
            id = apu.id,
            term = apu.term,
            bbkModel = bbk,
        )
    }

    suspend fun toDto(apu: ApuModel) = ApuDto(
        id = apu.id,
        term = apu.term,
        bbkId = apu.bbkModel.id
    )
}
