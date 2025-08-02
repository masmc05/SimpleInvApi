
plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("de.eldoria.plugin-yml.paper") version "0.7.1"
    id("com.gradleup.shadow") version "9.0.0-rc3"
    id("xyz.jpenilla.run-paper") version "2.3.1" // Adds runServer and runMojangMappedServer tasks for testing
}
group = "dev.masmc05"
version = "0.1-SNAPSHOT"

java {
    // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}
dependencies {
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")
    implementation(projects.api)
}

tasks {
    // Configure reobfJar to run when invoking the build task
    assemble {
        dependsOn(shadowJar)
    }
    runServer {
        minecraftVersion("1.21.8")
        systemProperty("terminal.jline", false)
        systemProperty("terminal.ansi", true)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        options.release.set(21)
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
    main = "dev.masmc05.invapi.Plugin"
    apiVersion = "1.21.8"
    author = "masmc05"
}
