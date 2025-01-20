package com.example.example;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import java.util.List;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

public class ExampleLoader implements PluginLoader {
  private static final RemoteRepository MAVEN_CENTRAL =
      newRepository("central", "https://repo1.maven.org/maven2/");
  private static final RemoteRepository PAPER_MC =
      newRepository("papermc", "https://repo.papermc.io/repository/maven-public/");
  private static final RemoteRepository CODE_MC =
      newRepository("codemc", "https://repo.codemc.org/repository/maven-public/");

  private static final String KOTLIN_VERSION = "2.1.0";
  private static final String KOTLIN_GROUP = "org.jetbrains.kotlin";

  private static final List<String> RUNTIME_DEPENDENCIES =
      List.of(
          KOTLIN_GROUP + ":kotlin-stdlib:" + KOTLIN_VERSION,
          KOTLIN_GROUP + ":kotlin-reflect:" + KOTLIN_VERSION,
          "dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.7.0",
          "dev.jorel:commandapi-bukkit-kotlin:9.7.0",
          "org.spongepowered:configurate-yaml:4.1.2",
          "org.spongepowered:configurate-extra-kotlin:4.1.2");

  @Override
  public void classloader(PluginClasspathBuilder classpathBuilder) {
    MavenLibraryResolver resolver = new MavenLibraryResolver();

    resolver.addRepository(MAVEN_CENTRAL);
    resolver.addRepository(PAPER_MC);
    resolver.addRepository(CODE_MC);

    for (String dep : RUNTIME_DEPENDENCIES) {
      resolver.addDependency(new Dependency(new DefaultArtifact(dep), null));
    }

    classpathBuilder.addLibrary(resolver);
  }

  private static RemoteRepository newRepository(String id, String url) {
    return new RemoteRepository.Builder(id, "default", url).build();
  }
}
