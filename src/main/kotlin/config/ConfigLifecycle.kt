package com.example.example.config

import com.example.example.Example
import com.example.example.config.exception.ConfigException

internal class ConfigLifecycle(private val plugin: Example) {
  private val provider = ConfigProvider(plugin.dataFolder)
  private val registry = ConfigRegistry()
  private val configFiles = mutableMapOf<Class<*>, String>()

  inline fun <reified T : Any> initialize(fileName: String): T {
    val config = provider.loadWithClass(fileName, T::class.java, plugin.pluginLogger)
    registry.register(T::class.java, config)
    configFiles[T::class.java] = fileName
    return config
  }

  inline fun <reified T : Any> get(): T = registry.get()

  fun reload() = try {
    registry.clear()
    configFiles.forEach { (clazz, fileName) ->
      val config = provider.loadWithClass(fileName, clazz, plugin.pluginLogger)
      registry.register(clazz, config)
    }
  } catch (e: Exception) {
    throw ConfigException("Failed to reload configurations", e)
  }

  fun save() = configFiles.forEach { (clazz, fileName) ->
    registry.get<Any>().takeIf { it::class.java == clazz }?.let { config ->
      provider.save(fileName, config)
    }
  }
}
