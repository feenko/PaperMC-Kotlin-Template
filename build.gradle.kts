plugins {
  kotlin("jvm") version "2.1.20-Beta1"
  id("xyz.jpenilla.run-paper") version "2.3.1"
  id("com.diffplug.spotless") version "7.0.2"
  id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
  paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")

  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))

  implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.7.0")
  implementation("dev.jorel:commandapi-bukkit-kotlin:9.7.0")

  implementation("org.spongepowered:configurate-yaml:4.1.2")
  implementation("org.spongepowered:configurate-extra-kotlin:4.1.2")
}

kotlin {
  jvmToolchain(21)
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

tasks {
  processResources {
    filesMatching("paper-plugin.yml") {
      expand("version" to version)
    }
  }

  runServer {
    minecraftVersion("1.21.4")
  }
}

spotless {
  kotlin {
    ktlint("1.5.0")
  }

  kotlinGradle {
    ktlint("1.5.0")
  }

  java {
    googleJavaFormat()
    removeUnusedImports()
    trimTrailingWhitespace()
    endWithNewline()
  }

  yaml {
    target("src/main/resources/**/*.yml")
    leadingTabsToSpaces(2)
  }
}
