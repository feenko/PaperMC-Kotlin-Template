package com.example.example.config

import eu.okaeri.configs.OkaeriConfig
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

internal class ConfigRegistry {
  private val configMap = ConcurrentHashMap<KClass<out OkaeriConfig>, OkaeriConfig>()
  private val readLock = Any()

  fun <T : OkaeriConfig> register(configClass: Class<T>, config: T) {
    configMap[configClass.kotlin] = config
  }

  inline fun <reified T : OkaeriConfig> get(): T = synchronized(readLock) {
    configMap[T::class]?.let { it as T }
      ?: error("Config ${T::class.simpleName} not found")
  }

  fun getAll(): Collection<OkaeriConfig> = configMap.values.toList()

  fun clear() {
    synchronized(readLock) {
      configMap.clear()
    }
  }

  fun size(): Int = configMap.size
}
