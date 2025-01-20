package com.example.example.config.model

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class MessagesConfig(
  var helloWorld: String = "<green>Hello World!</green>",
  var reloadSuccess: String = "<green>Configuration reloaded successfully!</green>",
)
