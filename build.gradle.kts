plugins {
  kotlin("jvm") version "2.1.20-Beta1"
  id("xyz.jpenilla.run-paper") version "2.3.1"
  id("com.diffplug.spotless") version "7.0.2"
  id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
  id("com.gradleup.shadow") version "9.0.0-beta4"
}

group = "com.example"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  maven("https://repo.papermc.io/repository/maven-public/")
  maven("https://repo.codemc.org/repository/maven-public/")
  maven("https://storehouse.okaeri.eu/repository/maven-public/")
}

dependencies {
  paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")

  implementation(kotlin("stdlib"))

  implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.7.0")
  implementation("dev.jorel:commandapi-bukkit-kotlin:9.7.0")

  implementation("eu.okaeri:okaeri-configs-yaml-bukkit:5.0.5")
  implementation("eu.okaeri:okaeri-validator:2.0.4")
}

kotlin {
  jvmToolchain(21)
}

tasks {
  shadowJar {
    minimize()
    mergeServiceFiles()

    dependencies {
      include(dependency("org.jetbrains.kotlin:.*"))
    }

    relocate("kotlin", "com.example.example.shadow.kotlin")
  }

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
    ktlint("1.5.0").setEditorConfigPath("$rootDir/.editorconfig")
  }

  kotlinGradle {
    ktlint("1.5.0").setEditorConfigPath("$rootDir/.editorconfig")
  }

  yaml {
    target("src/main/resources/**/*.yml")
    leadingTabsToSpaces(2)
  }
}
