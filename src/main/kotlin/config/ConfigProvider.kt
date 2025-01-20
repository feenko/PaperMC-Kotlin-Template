package com.example.example.config

import com.example.example.Logger
import com.example.example.config.exception.ConfigException
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.objectMapperFactory
import org.spongepowered.configurate.yaml.NodeStyle
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.io.File
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap

class ConfigProvider(private val dataFolder: File) {
  private val loaderCache = ConcurrentHashMap<Path, YamlConfigurationLoader>()
  
  private fun getLoader(path: Path): YamlConfigurationLoader = loaderCache.computeIfAbsent(path) {
    YamlConfigurationLoader.builder()
      .path(path)
      .defaultOptions { opts -> opts.serializers { it.registerAnnotatedObjects(objectMapperFactory()) } }
      .nodeStyle(NodeStyle.BLOCK)
      .indent(2)
      .build()
  }

  private fun compareNodes(
    existing: ConfigurationNode,
    default: ConfigurationNode,
    path: String = "",
  ): List<String> = buildList {
    existing.childrenMap().forEach { (key, _) ->
      val currentPath = if (path.isEmpty()) key.toString() else "$path.$key"
      if (default.node(key).virtual()) add("Removed: $currentPath")
    }

    default.childrenMap().forEach { (key, defaultValue) ->
      val currentPath = if (path.isEmpty()) key.toString() else "$path.$key"
      val existingValue = existing.node(key)

      when {
        existingValue.virtual() -> add("Added: $currentPath")
        defaultValue.hasChild() -> addAll(compareNodes(existingValue, defaultValue, currentPath))
      }
    }
  }

  fun <T : Any> loadWithClass(fileName: String, clazz: Class<T>, logger: Logger): T {
    val configFile = File(dataFolder, fileName).apply { parentFile.mkdirs() }
    val loader = getLoader(configFile.toPath())

    return try {
      val existingNode = loader.load()
      val defaultNode = loader.createNode().apply {
        set(clazz.getDeclaredConstructor().newInstance())
      }

      val changes = compareNodes(existingNode, defaultNode)
      if (changes.isNotEmpty()) {
        logger.info("Config updates for $fileName:")
        changes.forEach { change -> logger.info("- $change") }
        existingNode.mergeFrom(defaultNode)
        loader.save(existingNode)
      }

      existingNode.get(clazz) ?: throw ConfigException("Failed to deserialize config: $fileName")
    } catch (e: Exception) {
      throw ConfigException("Failed to load config: $fileName", e)
    }
  }

  fun save(fileName: String, config: Any) {
    val configFile = File(dataFolder, fileName)
    try {
      getLoader(configFile.toPath()).createNode().apply {
        set(config::class.java, config)
        getLoader(configFile.toPath()).save(this)
      }
    } catch (e: Exception) {
      throw ConfigException("Failed to save config: $fileName", e)
    }
  }
}
