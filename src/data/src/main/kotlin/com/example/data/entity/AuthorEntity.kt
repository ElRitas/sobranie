package com.example.data.entity

import org.jetbrains.exposed.dao.id.UUIDTable

object AuthorEntity : UUIDTable("author") {
    val name = varchar("name", 255)
}
