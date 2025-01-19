package com.example.example

import com.example.example.command.ExampleCommand
import com.example.example.config.ConfigService
import com.example.example.config.model.MessagesConfig
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import org.bukkit.plugin.java.JavaPlugin

class Example : JavaPlugin() {
  internal lateinit var configService: ConfigService
    private set

  val messagesConfig: MessagesConfig
    get() = configService.getConfig()

  override fun onLoad() {
    CommandAPI.onLoad(CommandAPIBukkitConfig(this))

    configService = ConfigService(this).apply {
      createConfig<MessagesConfig>("messages.yml")
    }
  }

  override fun onEnable() {
    CommandAPI.onEnable()
    ExampleCommand(this).register()
  }

  override fun onDisable() {
    configService.saveAllConfigs()
  }
}
