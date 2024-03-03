plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.7"
}

group = "dev.masmc05"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.20.4-R0.1-SNAPSHOT")
    compileOnly(projects.api)
}