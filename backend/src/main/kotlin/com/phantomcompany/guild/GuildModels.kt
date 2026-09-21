package com.phantomcompany.guild

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GuildType {
    @SerialName("training") TRAINING,
    @SerialName("raiding") RAIDING,
    @SerialName("rehab") REHAB,
}

@Serializable
data class CreateGuildRequest(val type: GuildType)

@Serializable
data class GuildComponentSet(
    val facility: String,
    val doctrine: String,
    val staff: String,
    val relic: String,
)

@Serializable
data class Hero(
    val name: String,
    val heroClass: String,
    val capability: Int,
    val control: Int,
    val stamina: Int,
    val classEnhancements: List<String>,
    val enhancements: List<String> = emptyList(),
    val drawbacks: List<String> = emptyList(),
)

@Serializable
data class Guild(
    val id: String,
    val type: GuildType,
    val gold: Int,
    val reputation: Int,
    val renown: Int,
    val components: GuildComponentSet,
    val roster: List<Hero>,
)
