package com.example.example

import com.example.example.command.ExampleCommand
import com.example.example.config.ConfigLifecycle
import com.example.example.config.model.MessagesConfig
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import org.bukkit.plugin.java.JavaPlugin

class Example : JavaPlugin() {
  private lateinit var configLifecycle: ConfigLifecycle

  val messagesConfig: MessagesConfig
    get() = configLifecycle.get()

  fun reloadConfigs() {
    try {
      configLifecycle.reload()
    } catch (e: Exception) {
      logger.severe("Failed to reload configs: ${e.message}")
      e.printStackTrace()
      configLifecycle.initialize<MessagesConfig>("messages.yml")
    }
  }

  override fun onLoad() {
    CommandAPI.onLoad(CommandAPIBukkitConfig(this))
    configLifecycle = ConfigLifecycle(this)
    configLifecycle.initialize<MessagesConfig>("messages.yml")
  }

  override fun onEnable() {
    CommandAPI.onEnable()
    ExampleCommand(this).register()
  }

  override fun onDisable() {
    CommandAPI.onDisable()
    configLifecycle.save()
  }
}
