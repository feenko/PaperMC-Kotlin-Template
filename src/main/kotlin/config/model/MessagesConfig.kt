package com.example.example.config.model

import eu.okaeri.configs.OkaeriConfig
import eu.okaeri.configs.annotation.Comment
import eu.okaeri.configs.annotation.Header

@Header("# Example Messages Config")
class MessagesConfig : OkaeriConfig() {
  @Comment("Message shown when using /example hello command")
  var helloWorld: String = "<green>Hello World!</green>"

  @Comment("Message shown when configuration is reloaded")
  var reloadSuccess: String = "<green>Configuration reloaded successfully!</green>"
}
