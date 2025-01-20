package com.example.example.config

import com.example.example.config.exception.ConfigException
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

internal class ConfigRegistry {
  private val configs = ConcurrentHashMap<KClass<*>, Any>()

  @Synchronized
  fun <T : Any> register(
    configClass: Class<T>,
    config: T,
  ) {
    require(configClass.isInstance(config)) {
      buildString {
        append("Config instance of type '${config::class.simpleName}' ")
        append("does not match expected class '${configClass.simpleName}'")
      }
    }
    configs[configClass.kotlin] = config
  }

  @Suppress("UNCHECKED_CAST")
  inline fun <reified T : Any> get(): T =
    configs[T::class] as? T
      ?: throw ConfigException(
        buildString {
          append("Config '${T::class.simpleName}' not found. ")
          append("Did you forget to initialize it?")
        },
      )

  @Synchronized
  fun clear() = configs.clear()

  fun getAll(): List<Any> = configs.values.toList()

  inline fun <reified T : Any> exists(): Boolean = configs.containsKey(T::class)
}
