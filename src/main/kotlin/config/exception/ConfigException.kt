package com.example.example.config.exception

class ConfigException(
  message: String,
  cause: Throwable? = null,
) : RuntimeException(message, cause)
