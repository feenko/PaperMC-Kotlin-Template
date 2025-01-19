package com.example.example.config

import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.yaml.NodeStyle
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.io.File
import java.nio.file.Path

internal class ConfigProvider(private val dataFolder: File) {
  private val mapper = objectMapperFactory()

  inline fun <reified T : Any> load(fileName: String): T {
    val configFile = File(dataFolder, fileName)
    configFile.parentFile.mkdirs()

    val loader = createLoader(configFile.toPath())

    if (!configFile.exists()) {
      initializeConfig<T>(loader)
    }

    return loader.load().get(T::class.java)
      ?: throw IllegalStateException("Failed to load config: $fileName")
  }

  fun save(fileName: String, config: Any) {
    val configFile = File(dataFolder, fileName)
    val loader = createLoader(configFile.toPath())
    val node = loader.createNode()
    node.set(config)
    loader.save(node)
  }

  @Suppress("UNCHECKED_CAST")
  fun <T : Any> loadWithClass(fileName: String, clazz: Class<out T>): T {
    val configFile = File(dataFolder, fileName)
    val loader = createLoader(configFile.toPath())
    return loader.load().get(clazz)
      ?: throw IllegalStateException("Failed to load config: $fileName")
  }

  private fun createLoader(path: Path): YamlConfigurationLoader = YamlConfigurationLoader.builder()
    .path(path)
    .defaultOptions { opts ->
      opts.serializers { builder ->
        builder.registerAnnotatedObjects(mapper)
      }
    }
    .nodeStyle(NodeStyle.BLOCK)
    .build()

  private inline fun <reified T : Any> initializeConfig(loader: YamlConfigurationLoader) {
    val node = loader.createNode()
    node.set(T::class.java, T::class.java.getDeclaredConstructor().newInstance())
    loader.save(node)
  }
}
