package com.phantomcompany.persistence

import com.phantomcompany.guild.Guild
import com.phantomcompany.guild.GuildRepository
import java.sql.Connection
import java.util.UUID
import javax.sql.DataSource

class PostgresGuildRepository(private val dataSource: DataSource) : GuildRepository {
    override fun save(guild: Guild): Guild = dataSource.connection.use { connection ->
        try {
            insertGuild(connection, guild)
            insertComponents(connection, guild)
            insertRoster(connection, guild)
            connection.commit()
            guild
        } catch (exception: Exception) {
            connection.rollback()
            throw exception
        }
    }

    private fun insertGuild(connection: Connection, guild: Guild) {
        connection.prepareStatement(
            """
            INSERT INTO guilds (id, guild_type, gold, reputation, renown)
            VALUES (?, ?, ?, ?, ?)
            """.trimIndent(),
        ).use { statement ->
            statement.setObject(1, UUID.fromString(guild.id))
            statement.setString(2, guild.type.name.lowercase())
            statement.setInt(3, guild.gold)
            statement.setInt(4, guild.reputation)
            statement.setInt(5, guild.renown)
            statement.executeUpdate()
        }
    }

    private fun insertComponents(connection: Connection, guild: Guild) {
        val components = listOf(
            "facility" to guild.components.facility,
            "doctrine" to guild.components.doctrine,
            "staff" to guild.components.staff,
            "relic" to guild.components.relic,
        )

        connection.prepareStatement(
            "INSERT INTO guild_components (guild_id, component_type, content_key) VALUES (?, ?, ?)",
        ).use { statement ->
            components.forEach { (type, key) ->
                statement.setObject(1, UUID.fromString(guild.id))
                statement.setString(2, type)
                statement.setString(3, key)
                statement.addBatch()
            }
            statement.executeBatch()
        }
    }

    private fun insertRoster(connection: Connection, guild: Guild) {
        guild.roster.forEach { hero ->
            val heroId = UUID.randomUUID()
            connection.prepareStatement(
                """
                INSERT INTO heroes (id, guild_id, name, hero_class, capability, control, stamina)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """.trimIndent(),
            ).use { statement ->
                statement.setObject(1, heroId)
                statement.setObject(2, UUID.fromString(guild.id))
                statement.setString(3, hero.name)
                statement.setString(4, hero.heroClass)
                statement.setInt(5, hero.capability)
                statement.setInt(6, hero.control)
                statement.setInt(7, hero.stamina)
                statement.executeUpdate()
            }

            val traits = hero.classEnhancements.map { Triple("class_enhancement", it, true) } +
                hero.enhancements.map { Triple("enhancement", it, false) } +
                hero.drawbacks.map { Triple("drawback", it, false) }

            connection.prepareStatement(
                "INSERT INTO hero_traits (hero_id, trait_type, content_key, class_granted) VALUES (?, ?, ?, ?)",
            ).use { statement ->
                traits.forEach { (type, key, classGranted) ->
                    statement.setObject(1, heroId)
                    statement.setString(2, type)
                    statement.setString(3, key)
                    statement.setBoolean(4, classGranted)
                    statement.addBatch()
                }
                statement.executeBatch()
            }
        }
    }
}
