package com.example.example.config.model

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
class MessagesConfig(
  val hello: Hello = Hello(),
  val reload: Reload = Reload(),
  val placeholder: String = "<gray>Placeholder</gray>",
) {
  @ConfigSerializable
  class Hello(
    val world: String = "<green>Hello World!</green>",
  )

  @ConfigSerializable
  class Reload(
    @Comment("The message to send when the configuration is reloaded successfully.")
    val success: String = "<green>Configuration reloaded successfully!</green>",
    @Comment("The message to send when the configuration is reloaded successfully.")
    val error: String = "<red>Failed to reload configuration!</red>",
  )
}
