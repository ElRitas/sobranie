package com.example.app.route.v1

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class FavoriteResponse(
    private val userId: @Contextual UUID,
    private val bookId: @Contextual UUID,
) {
    constructor(idPair: Pair<UUID, UUID>) : this(idPair.first, idPair.second)
}
