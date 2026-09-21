package com.phantomcompany

import com.phantomcompany.guild.configureGuildRoutes
import com.phantomcompany.guild.GuildService
import com.phantomcompany.persistence.DatabaseFactory
import com.phantomcompany.persistence.PostgresGuildRepository
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, host = "0.0.0.0", port = 8080, module = Application::module)
        .start(wait = true)
}

fun Application.module(service: GuildService? = null) {
    val dataSource = if (service == null) DatabaseFactory.createDataSource() else null
    val guildService = service ?: GuildService(PostgresGuildRepository(dataSource!!))

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = false
        })
    }

    install(CORS) {
        allowHost("localhost:5173", schemes = listOf("http"))
        allowHost("127.0.0.1:5173", schemes = listOf("http"))
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.ContentType)
    }

    routing {
        get("/api/health") {
            call.respond(HealthResponse(status = "ok", service = "phantom-company"))
        }
        configureGuildRoutes(guildService)
    }

    dataSource?.let { pool ->
        monitor.subscribe(io.ktor.server.application.ApplicationStopped) {
            pool.close()
        }
    }
}
