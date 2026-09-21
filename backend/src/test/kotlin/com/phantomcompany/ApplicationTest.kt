package com.phantomcompany

import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import com.phantomcompany.guild.CreateGuildRequest
import com.phantomcompany.guild.Guild
import com.phantomcompany.guild.GuildType
import com.phantomcompany.guild.GuildService
import com.phantomcompany.guild.InMemoryGuildRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    private fun testService() = GuildService(InMemoryGuildRepository())

    @Test
    fun `health endpoint reports ready`() = testApplication {
        application { module(testService()) }

        val response = client.get("/api/health")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `each guild type receives its expected starting state`() = testApplication {
        application { module(testService()) }
        val jsonClient = createClient {
            install(ContentNegotiation) { json() }
        }

        val expectedRelics = mapOf(
            GuildType.TRAINING to "Statue",
            GuildType.RAIDING to "Skull",
            GuildType.REHAB to "Banner",
        )

        expectedRelics.forEach { (type, relic) ->
            val response = jsonClient.post("/api/guilds") {
                contentType(ContentType.Application.Json)
                setBody(CreateGuildRequest(type))
            }
            val guild = response.body<Guild>()

            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals(10_000, guild.gold)
            assertEquals(relic, guild.components.relic)
            assertEquals("Phantom Squire", guild.roster.single().name)
            assertEquals(listOf("Throw Rock"), guild.roster.single().classEnhancements)
        }
    }
}
