
plugins {
    `java-library`
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.2.2" // Adds runServer and runMojangMappedServer tasks for testing
}
group = "dev.masmc05"
version = "0.1-SNAPSHOT"

java {
    // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}
dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    implementation(projects.api)
    implementation(project(":v1_20_4", "reobf"))
    implementation(project(":v1_20_2", "reobf"))
}

tasks {
    // Configure reobfJar to run when invoking the build task
    assemble {
        dependsOn(shadowJar)
    }
    runServer {
        minecraftVersion("1.20.4")
        systemProperty("terminal.jline", false)
        systemProperty("terminal.ansi", true)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
}
paper {
    name = "SimpleInvApi"
    version = " "
    main = "dev.masmc05.invapi.plugin.Plugin"
    apiVersion = "1.20"
    author = "masmc05"
    generateLibrariesJson = true
}
