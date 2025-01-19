package com.example.example

import io.papermc.paper.plugin.loader.PluginClasspathBuilder
import io.papermc.paper.plugin.loader.PluginLoader
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.repository.RemoteRepository

class ExampleLoader : PluginLoader {
  override fun classloader(classpathBuilder: PluginClasspathBuilder) {
    val resolver =
      MavenLibraryResolver().apply {
        listOf(
          "codemc" to "https://repo.codemc.org/repository/maven-public/",
          "okaeri" to "https://storehouse.okaeri.eu/repository/maven-public/",
        )
          .forEach { (id, url) ->
            addRepository(RemoteRepository.Builder(id, "default", url).build())
          }

        listOf(
          "dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.7.0",
          "dev.jorel:commandapi-bukkit-kotlin:9.7.0",
          "eu.okaeri:okaeri-configs-yaml-bukkit:5.0.5",
          "eu.okaeri:okaeri-validator:2.0.4",
        )
          .forEach { dependency ->
            addDependency(Dependency(DefaultArtifact(dependency), null))
          }
      }

    classpathBuilder.addLibrary(resolver)
  }
}
