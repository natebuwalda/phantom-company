val ktorVersion = "3.6.0"

plugins {
    kotlin("jvm") version "2.4.20"
    kotlin("plugin.serialization") version "2.4.20"
    id("io.ktor.plugin") version "3.6.0"
    application
}

group = "com.phantomcompany"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-cors-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("com.zaxxer:HikariCP:7.1.0")
    implementation("org.postgresql:postgresql:42.7.12")
    implementation("org.flywaydb:flyway-core:12.11.0")
    implementation("org.flywaydb:flyway-database-postgresql:12.11.0")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.20")

    testImplementation("io.ktor:ktor-server-test-host-jvm:$ktorVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation-jvm:$ktorVersion")
    testImplementation(kotlin("test"))
}

val acceptanceTestSourceSet = sourceSets.create("acceptanceTest") {
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += output + compileClasspath
}

configurations[acceptanceTestSourceSet.implementationConfigurationName]
    .extendsFrom(configurations.testImplementation.get())
configurations[acceptanceTestSourceSet.runtimeOnlyConfigurationName]
    .extendsFrom(configurations.testRuntimeOnly.get())

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("com.phantomcompany.ApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("acceptanceTest") {
    description = "Runs API acceptance tests against a real PostgreSQL database."
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    testClassesDirs = acceptanceTestSourceSet.output.classesDirs
    classpath = acceptanceTestSourceSet.runtimeClasspath
    shouldRunAfter(tasks.test)
    useJUnitPlatform()
}

ktor {
    fatJar {
        archiveFileName.set("phantom-company.jar")
    }
}
