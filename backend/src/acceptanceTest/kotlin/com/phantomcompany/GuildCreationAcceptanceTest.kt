package com.phantomcompany

import com.phantomcompany.guild.CreateGuildRequest
import com.phantomcompany.guild.Guild
import com.phantomcompany.guild.GuildType
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import java.sql.DriverManager
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class GuildCreationAcceptanceTest {
    @Test
    fun `training guild is persisted with its complete starting state`() = testApplication {
        requireDatabaseEnvironment()
        application { module() }
        val jsonClient = createClient {
            install(ContentNegotiation) { json() }
        }

        val response = jsonClient.post("/api/guilds") {
            contentType(ContentType.Application.Json)
            setBody(CreateGuildRequest(GuildType.TRAINING))
        }
        val guild = response.body<Guild>()

        try {
            assertEquals(HttpStatusCode.Created, response.status)
            assertEquals(10_000, guild.gold)
            assertEquals("Barracks", guild.components.facility)
            assertEquals("Regimen", guild.components.doctrine)
            assertEquals("Veteran", guild.components.staff)
            assertEquals("Statue", guild.components.relic)

            val hero = guild.roster.single()
            assertEquals("Phantom Squire", hero.name)
            assertEquals("Squire", hero.heroClass)
            assertEquals(listOf(1, 1, 1), listOf(hero.capability, hero.control, hero.stamina))
            assertEquals(listOf("Throw Rock"), hero.classEnhancements)

            assertPersistedState(guild.id)
        } finally {
            deleteGuild(guild.id)
        }
    }

    private fun assertPersistedState(guildId: String) = connection().use { connection ->
        connection.prepareStatement(
            """
            SELECT g.guild_type, g.gold,
                   COUNT(DISTINCT c.component_type) AS component_count,
                   COUNT(DISTINCT h.id) AS hero_count,
                   COUNT(DISTINCT t.content_key) FILTER (WHERE t.trait_type = 'class_enhancement') AS class_trait_count
            FROM guilds g
            LEFT JOIN guild_components c ON c.guild_id = g.id
            LEFT JOIN heroes h ON h.guild_id = g.id
            LEFT JOIN hero_traits t ON t.hero_id = h.id
            WHERE g.id = ?
            GROUP BY g.id
            """.trimIndent(),
        ).use { statement ->
            statement.setObject(1, UUID.fromString(guildId))
            statement.executeQuery().use { result ->
                check(result.next()) { "Created guild was not found in PostgreSQL" }
                assertEquals("training", result.getString("guild_type"))
                assertEquals(10_000, result.getInt("gold"))
                assertEquals(4, result.getInt("component_count"))
                assertEquals(1, result.getInt("hero_count"))
                assertEquals(1, result.getInt("class_trait_count"))
            }
        }
    }

    private fun deleteGuild(guildId: String) = connection().use { connection ->
        connection.prepareStatement("DELETE FROM guilds WHERE id = ?").use { statement ->
            statement.setObject(1, UUID.fromString(guildId))
            statement.executeUpdate()
        }
    }

    private fun connection() = DriverManager.getConnection(
        requiredEnvironment("PHANTOM_DB_URL"),
        requiredEnvironment("PHANTOM_DB_USER"),
        requiredEnvironment("PHANTOM_DB_PASSWORD"),
    )

    private fun requireDatabaseEnvironment() {
        requiredEnvironment("PHANTOM_DB_URL")
        requiredEnvironment("PHANTOM_DB_USER")
        requiredEnvironment("PHANTOM_DB_PASSWORD")
    }

    private fun requiredEnvironment(name: String): String =
        System.getenv(name)?.takeIf(String::isNotBlank)
            ?: error("$name must be set before running the acceptanceTest task")
}
