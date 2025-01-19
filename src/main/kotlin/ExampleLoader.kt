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
          "mavenCentral" to "https://repo1.maven.org/maven2/",
          "codeMc" to "https://repo.codemc.org/repository/maven-public/",
        )
          .forEach { (id, url) ->
            addRepository(RemoteRepository.Builder(id, "default", url).build())
          }

        listOf(
          "dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.7.0",
          "dev.jorel:commandapi-bukkit-kotlin:9.7.0",
          "org.spongepowered:configurate-yaml:4.1.2",
          "org.spongepowered:configurate-extra-kotlin:4.1.2",
        )
          .forEach { dependency ->
            addDependency(Dependency(DefaultArtifact(dependency), null))
          }
      }

    classpathBuilder.addLibrary(resolver)
  }
}
