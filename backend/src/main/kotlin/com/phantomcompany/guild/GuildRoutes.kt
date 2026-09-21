package com.phantomcompany.guild

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.configureGuildRoutes(service: GuildService) {
    post("/api/guilds") {
        val request = call.receive<CreateGuildRequest>()
        call.respond(HttpStatusCode.Created, service.create(request.type))
    }
}
