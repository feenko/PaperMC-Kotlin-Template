package com.example.example.config.model

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Comment

@ConfigSerializable
data class MessagesConfig(
  @Comment("Message shown when using /example hello command")
  val helloWorld: String = "<green>Hello World!</green>",

  @Comment("Message shown when configuration is reloaded")
  val reloadSuccess: String = "<green>Configuration reloaded successfully!</green>",
)
