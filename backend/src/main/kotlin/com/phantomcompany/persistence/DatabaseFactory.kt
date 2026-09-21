package com.phantomcompany.persistence

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway

object DatabaseFactory {
    fun createDataSource(): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl = env("PHANTOM_DB_URL", "jdbc:postgresql://127.0.0.1:5432/phantom_company")
            username = env("PHANTOM_DB_USER", "phantom_app")
            password = requiredEnv("PHANTOM_DB_PASSWORD")
            driverClassName = "org.postgresql.Driver"
            maximumPoolSize = 5
            minimumIdle = 1
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_READ_COMMITTED"
            addDataSourceProperty("tcpKeepAlive", "true")
            validate()
        }

        return HikariDataSource(config).also { dataSource ->
            Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load()
                .migrate()
        }
    }

    private fun env(name: String, fallback: String): String =
        System.getenv(name)?.takeIf(String::isNotBlank) ?: fallback

    private fun requiredEnv(name: String): String =
        System.getenv(name)?.takeIf(String::isNotBlank)
            ?: error("Required environment variable $name is not set")
}
