package com.example.example.command

import com.example.example.Example
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import net.kyori.adventure.text.minimessage.MiniMessage

class ExampleCommand(
  private val plugin: Example,
) {
  private val mm = MiniMessage.miniMessage()

  fun register() {
    CommandAPICommand("example")
      .withSubcommand(
        CommandAPICommand("hello")
          .withPermission("example.hello")
          .executes(
            CommandExecutor { sender, _ ->
              sender.sendMessage(mm.deserialize(plugin.messagesConfig.hello.world))
            },
          ),
      ).withSubcommand(
        CommandAPICommand("reload")
          .withPermission("example.reload")
          .executes(
            CommandExecutor { sender, _ ->
              plugin.reloadConfigs()
              sender.sendMessage(mm.deserialize(plugin.messagesConfig.reload.success))
            },
          ),
      ).register()
  }
}
