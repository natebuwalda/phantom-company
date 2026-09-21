export type GuildType = 'training' | 'raiding' | 'rehab'

export interface GuildDefinition {
  name: string
  belief: string
  opportunity: string
  facility: string
  doctrine: string
  staff: string
  relic: string
  mark: string
}

export const guildTypes: GuildType[] = ['training', 'raiding', 'rehab']

export const guildDefinitions: Record<GuildType, GuildDefinition> = {
  training: {
    name: 'Training Guild',
    belief: 'Potential is undervalued.',
    opportunity: 'Develop overlooked recruits into exceptional heroes.',
    facility: 'Barracks',
    doctrine: 'Regimen',
    staff: 'Veteran',
    relic: 'Statue',
    mark: 'I',
  },
  raiding: {
    name: 'Raiding Guild',
    belief: 'Immediate strength creates opportunity.',
    opportunity: 'Accept greater danger for access to greater rewards.',
    facility: 'Trophy Hall',
    doctrine: 'Party',
    staff: 'Quartermaster',
    relic: 'Skull',
    mark: 'II',
  },
  rehab: {
    name: 'Rehab Guild',
    belief: 'Broken heroes are overlooked assets.',
    opportunity: 'Restore heroes other guilds have cast aside.',
    facility: 'Hospital',
    doctrine: 'Succor',
    staff: 'Chirurgeon',
    relic: 'Banner',
    mark: 'III',
  },
}

export const phantomSquire = {
  name: 'Phantom Squire',
  className: 'Squire',
  capability: 1,
  control: 1,
  stamina: 1,
  classEnhancement: 'Throw Rock',
} as const

export function createGuild(type: GuildType) {
  return {
    type,
    definition: guildDefinitions[type],
    gold: 10_000,
    reputation: 0,
    renown: 0,
    hero: phantomSquire,
  }
}
