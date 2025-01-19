package com.example.example.config

import eu.okaeri.configs.ConfigManager
import eu.okaeri.configs.OkaeriConfig
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer
import java.io.File

internal class ConfigFactory(private val dataFolder: File) {
  inline fun <reified T : OkaeriConfig> create(fileName: String): T = ConfigManager.create(T::class.java) {
    it.withConfigurer(YamlBukkitConfigurer())
      .withBindFile(File(dataFolder, fileName))
      .withRemoveOrphans(true)
      .saveDefaults()
      .load(true)
  }
}
