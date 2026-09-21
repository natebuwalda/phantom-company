import { useState } from 'react'
import './App.css'
import {
  createGuild,
  guildDefinitions,
  guildTypes,
  type GuildType,
} from './game'

function App() {
  const [selectedType, setSelectedType] = useState<GuildType>('training')
  const [guildType, setGuildType] = useState<GuildType | null>(null)
  const guild = guildType ? createGuild(guildType) : null

  return (
    <main className="shell">
      <header className="masthead">
        <p className="eyebrow">Minimum viable expedition</p>
        <h1>Phantom Company</h1>
        <p className="lede">
          Build the institution behind the heroes. Choose the belief that will
          shape your first guild.
        </p>
      </header>

      {!guild ? (
        <section className="founding" aria-labelledby="founding-title">
          <div className="section-heading">
            <p className="step">Founding charter · 01</p>
            <h2 id="founding-title">Choose a guild philosophy</h2>
          </div>

          <div className="guild-grid" role="radiogroup" aria-label="Guild type">
            {guildTypes.map((type) => {
              const definition = guildDefinitions[type]
              const selected = type === selectedType

              return (
                <button
                  className={`guild-card${selected ? ' selected' : ''}`}
                  key={type}
                  type="button"
                  role="radio"
                  aria-checked={selected}
                  onClick={() => setSelectedType(type)}
                >
                  <span className="card-mark" aria-hidden="true">
                    {definition.mark}
                  </span>
                  <span className="card-kicker">{definition.facility}</span>
                  <strong>{definition.name}</strong>
                  <span className="belief">“{definition.belief}”</span>
                  <span className="opportunity">{definition.opportunity}</span>
                </button>
              )
            })}
          </div>

          <div className="charter-bar">
            <div>
              <span>Starting treasury</span>
              <strong>10,000 gold</strong>
            </div>
            <div>
              <span>Founding hero</span>
              <strong>Phantom Squire</strong>
            </div>
            <button
              className="primary-action"
              type="button"
              onClick={() => setGuildType(selectedType)}
            >
              Sign the charter
            </button>
          </div>
        </section>
      ) : (
        <section className="headquarters" aria-labelledby="headquarters-title">
          <div className="section-heading">
            <p className="step">Guild headquarters · established</p>
            <h2 id="headquarters-title">{guild.definition.name}</h2>
            <p>{guild.definition.belief}</p>
          </div>

          <div className="dashboard">
            <article className="panel treasury-panel">
              <span className="panel-label">Treasury</span>
              <strong className="gold">{guild.gold.toLocaleString()} gold</strong>
              <dl className="resources">
                <div><dt>Reputation</dt><dd>{guild.reputation}</dd></div>
                <div><dt>Renown</dt><dd>{guild.renown}</dd></div>
                <div><dt>Roster</dt><dd>1 / 20</dd></div>
              </dl>
            </article>

            <article className="panel hero-panel">
              <div className="portrait" aria-hidden="true">PS</div>
              <div>
                <span className="panel-label">Founding hero · Squire</span>
                <h3>{guild.hero.name}</h3>
                <div className="stats" aria-label="Hero statistics">
                  <span><b>{guild.hero.capability}</b> Capability</span>
                  <span><b>{guild.hero.control}</b> Control</span>
                  <span><b>{guild.hero.stamina}</b> Stamina</span>
                </div>
                <p className="enhancement">
                  <b>Class enhancement</b> {guild.hero.classEnhancement}
                </p>
              </div>
            </article>

            <article className="panel components-panel">
              <span className="panel-label">Founding components</span>
              <ul>
                <li><span>Doctrine</span><b>{guild.definition.doctrine}</b></li>
                <li><span>Staff</span><b>{guild.definition.staff}</b></li>
                <li><span>Relic</span><b>{guild.definition.relic}</b></li>
                <li><span>Facility</span><b>{guild.definition.facility}</b></li>
              </ul>
            </article>
          </div>

          <div className="next-step">
            <div>
              <span className="status-dot" aria-hidden="true" />
              Guild state created successfully
            </div>
            <button className="secondary-action" type="button" onClick={() => setGuildType(null)}>
              Start another guild
            </button>
          </div>
        </section>
      )}

      <footer>
        <span>The guild is the build.</span>
        <span>Prototype 0.1</span>
      </footer>
    </main>
  )
}

export default App
