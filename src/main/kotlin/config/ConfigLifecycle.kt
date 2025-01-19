package com.example.example.config

import com.example.example.Example
import kotlin.reflect.KClass

internal class ConfigLifecycle(plugin: Example) {
  private val provider = ConfigProvider(plugin.dataFolder)
  private val registry = ConfigRegistry()
  private val fileNames = mutableMapOf<KClass<*>, String>()

  inline fun <reified T : Any> initialize(fileName: String): T {
    val config = provider.load<T>(fileName)
    registry.register(T::class.java, config)
    fileNames[T::class] = fileName
    return config
  }

  fun reload() {
    registry.getAll().forEach { config ->
      val fileName = fileNames[config::class]
        ?: error("No filename registered for ${config::class.simpleName}")
      val reloaded = provider.loadWithClass(fileName, config::class.java)
      registry.register(config::class.java, reloaded)
    }
  }

  fun save() {
    registry.getAll().forEach { config ->
      val fileName = fileNames[config::class]
        ?: error("No filename registered for ${config::class.simpleName}")
      provider.save(fileName, config)
    }
  }

  inline fun <reified T : Any> get(): T = registry.get()
}
