package com.example.example.config

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

internal class ConfigRegistry {
  private val configs = ConcurrentHashMap<KClass<*>, Any>()

  fun <T : Any> register(configClass: Class<T>, config: Any) {
    if (!configClass.isInstance(config)) {
      throw IllegalArgumentException("Config instance does not match expected class")
    }
    configs[configClass.kotlin] = config
  }

  @Suppress("UNCHECKED_CAST")
  inline fun <reified T : Any> get(): T = configs[T::class] as? T
    ?: throw IllegalStateException("Config ${T::class.simpleName} not found")

  fun getAll(): List<Any> = configs.values.toList()
}
