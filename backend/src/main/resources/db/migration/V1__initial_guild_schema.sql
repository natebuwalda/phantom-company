CREATE TABLE guilds (
    id UUID PRIMARY KEY,
    guild_type VARCHAR(20) NOT NULL CHECK (guild_type IN ('training', 'raiding', 'rehab')),
    gold INTEGER NOT NULL CHECK (gold >= 0),
    reputation INTEGER NOT NULL DEFAULT 0,
    renown INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE guild_components (
    guild_id UUID NOT NULL REFERENCES guilds(id) ON DELETE CASCADE,
    component_type VARCHAR(24) NOT NULL CHECK (component_type IN ('facility', 'doctrine', 'staff', 'relic')),
    content_key VARCHAR(100) NOT NULL,
    PRIMARY KEY (guild_id, component_type)
);

CREATE TABLE heroes (
    id UUID PRIMARY KEY,
    guild_id UUID NOT NULL REFERENCES guilds(id) ON DELETE CASCADE,
    name VARCHAR(120) NOT NULL,
    hero_class VARCHAR(60) NOT NULL,
    capability SMALLINT NOT NULL CHECK (capability >= 0),
    control SMALLINT NOT NULL CHECK (control >= 0),
    stamina SMALLINT NOT NULL CHECK (stamina >= 0),
    available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX heroes_guild_id_index ON heroes(guild_id);

CREATE TABLE hero_traits (
    hero_id UUID NOT NULL REFERENCES heroes(id) ON DELETE CASCADE,
    trait_type VARCHAR(32) NOT NULL CHECK (trait_type IN ('class_enhancement', 'enhancement', 'drawback')),
    content_key VARCHAR(100) NOT NULL,
    class_granted BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (hero_id, trait_type, content_key)
);

CREATE TABLE expedition_snapshots (
    id UUID PRIMARY KEY,
    guild_id UUID NOT NULL REFERENCES guilds(id) ON DELETE CASCADE,
    sequence_number INTEGER NOT NULL CHECK (sequence_number > 0),
    state JSONB NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (guild_id, sequence_number)
);

CREATE INDEX expedition_snapshots_state_index ON expedition_snapshots USING GIN(state);
