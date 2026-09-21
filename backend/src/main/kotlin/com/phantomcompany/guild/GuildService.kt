package com.phantomcompany.guild

import java.util.UUID

private val componentSets = mapOf(
    GuildType.TRAINING to GuildComponentSet(
        facility = "Barracks",
        doctrine = "Regimen",
        staff = "Veteran",
        relic = "Statue",
    ),
    GuildType.RAIDING to GuildComponentSet(
        facility = "Trophy Hall",
        doctrine = "Party",
        staff = "Quartermaster",
        relic = "Skull",
    ),
    GuildType.REHAB to GuildComponentSet(
        facility = "Hospital",
        doctrine = "Succor",
        staff = "Chirurgeon",
        relic = "Banner",
    ),
)

private val phantomSquire = Hero(
    name = "Phantom Squire",
    heroClass = "Squire",
    capability = 1,
    control = 1,
    stamina = 1,
    classEnhancements = listOf("Throw Rock"),
)

interface GuildRepository {
    fun save(guild: Guild): Guild
}

class InMemoryGuildRepository : GuildRepository {
    private val guilds = mutableListOf<Guild>()

    override fun save(guild: Guild): Guild = guild.also(guilds::add)
}

class GuildService(private val repository: GuildRepository) {
    fun create(type: GuildType): Guild = repository.save(Guild(
        id = UUID.randomUUID().toString(),
        type = type,
        gold = 10_000,
        reputation = 0,
        renown = 0,
        components = componentSets.getValue(type),
        roster = listOf(phantomSquire),
    ))
}
