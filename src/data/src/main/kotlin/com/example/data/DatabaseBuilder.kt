package com.example.data

import com.example.data.entity.ApuEntity
import com.example.data.entity.AuthorEntity
import com.example.data.entity.BbkEntity
import com.example.data.entity.BookAuthorCrossRef
import com.example.data.entity.BookEntity
import com.example.data.entity.IssuanceEntity
import com.example.data.entity.PublisherEntity
import com.example.data.entity.QueueEntity
import com.example.data.entity.ReservationEntity
import com.example.data.entity.UserEntity
import com.example.data.entity.UserFavoriteCrossRef
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource
import kotlin.apply

object DatabaseBuilder {
    data class DatabaseConfig(
        val url: String,
        val driver: String,
        val username: String,
        val password: String,
        val maximumPoolSize: Int = 10,
    )

    fun connect(dataSource: DataSource): Database {
        return Database.connect(dataSource)
    }

    fun createDataSource(config: DatabaseConfig): HikariDataSource {
        val hikariConfig =
            HikariConfig().apply {
                jdbcUrl = config.url
                driverClassName = config.driver
                username = config.username
                password = config.password
                maximumPoolSize = config.maximumPoolSize
                connectionTimeout = 5000
                validationTimeout = 2500
            }
        return HikariDataSource(hikariConfig)
    }

    fun runMigrations(db: Database) {
        transaction(db) {
            SchemaUtils.createMissingTablesAndColumns(
                ApuEntity,
                AuthorEntity,
                BbkEntity,
                BookAuthorCrossRef,
                BookEntity,
                IssuanceEntity,
                PublisherEntity,
                ReservationEntity,
                QueueEntity,
                UserEntity,
                UserFavoriteCrossRef,
            )
        }
    }
}
