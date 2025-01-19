package com.example.example.config

import com.example.example.Example
import eu.okaeri.configs.OkaeriConfig
import kotlin.reflect.KClass

internal class ConfigService(plugin: Example) {
  private val configFactory = ConfigFactory(plugin.dataFolder)
  private val configRegistry = ConfigRegistry()
  private val configCache = mutableMapOf<KClass<out OkaeriConfig>, OkaeriConfig>()
  private val lazyConfigs = mutableMapOf<KClass<out OkaeriConfig>, Lazy<OkaeriConfig>>()

  inline fun <reified T : OkaeriConfig> createConfig(fileName: String): T {
    val configClass = T::class
    return lazyConfigs.getOrPut(configClass) {
      lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        configFactory.create<T>(fileName).also { config ->
          configRegistry.register(T::class.java, config)
          configCache[configClass] = config
        }
      }
    }.value as T
  }

  inline fun <reified T : OkaeriConfig> getConfig(): T {
    val configClass = T::class
    return configCache[configClass]?.let { it as T }
      ?: configRegistry.get<T>().also { config ->
        configCache[configClass] = config
      }
  }

  fun reloadAllConfigs() {
    configCache.clear()
    configRegistry.getAll().forEach { config ->
      config.apply {
        saveDefaults()
        load(true)
      }
    }
  }

  fun saveAllConfigs() = configRegistry.getAll().forEach { it.save() }

  inline fun <reified T : OkaeriConfig> saveConfig() = getConfig<T>().save()

  fun clearCache() {
    configCache.clear()
  }
}
