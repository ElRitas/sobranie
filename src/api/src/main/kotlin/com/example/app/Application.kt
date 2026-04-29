package com.example.app

import com.example.app.di.appModule
import com.example.app.plugin.configureLog
import com.example.app.plugin.configureSecurity
import com.example.app.plugin.configureSerialization
import com.example.app.plugin.configureStatusPages
import com.example.app.route.v1.apuRoutes
import com.example.app.route.v1.authRoutes
import com.example.app.route.v1.authorRoutes
import com.example.app.route.v1.bbkRoutes
import com.example.app.route.v1.bookRoutes
import com.example.app.route.v1.favoriteRoutes
import com.example.app.route.v1.issuanceRoutes
import com.example.app.route.v1.publisherRoutes
import com.example.app.route.v1.queueRoutes
import com.example.app.route.v1.reservationRoutes
import com.example.app.route.v1.userRoutes
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureLog()
    configureSerialization()
    configureSecurity()
    configureStatusPages()

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
    }
    install(Koin) {
        slf4jLogger()
        modules(appModule(environment.config))
    }
    routing {
        apuRoutes()
        authorRoutes()
        bbkRoutes()
        bookRoutes()
        publisherRoutes()
        userRoutes()
        reservationRoutes()
        queueRoutes()
        issuanceRoutes()
        favoriteRoutes()
        authRoutes()
    }
}
