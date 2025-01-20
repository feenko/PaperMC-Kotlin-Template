package com.example.example

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.plugin.Plugin

class Logger(
  private val plugin: Plugin,
) {
  private val mm = MiniMessage.miniMessage()
  private val prefix = "[${plugin.name}]"

  private fun log(
    message: String,
    color: NamedTextColor,
  ) {
    plugin.server.consoleSender.sendMessage(
      mm.deserialize("<${color.toString().lowercase()}>$prefix $message</${color.toString().lowercase()}>"),
    )
  }

  fun info(message: String) = log(message, NamedTextColor.AQUA)

  fun warning(message: String) = log(message, NamedTextColor.YELLOW)

  fun debug(message: String) = log(message, NamedTextColor.GRAY)

  fun severe(
    message: String,
    error: Throwable? = null,
  ) {
    log(message, NamedTextColor.RED)
    error?.let { err ->
      plugin.server.consoleSender.sendMessage(
        Component.text(err.stackTraceToString()).color(NamedTextColor.RED),
      )
    }
  }
}
