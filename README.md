# PaperMC Kotlin Template

A modern, feature-rich template for creating Minecraft plugins using PaperMC and Kotlin. This template provides a solid foundation for developing high-performance, maintainable plugins with the latest tools and best practices.

## Features

- [x] Modern [PaperMC](https://github.com/PaperMC/Paper) API
    - [x] Uses [Loaders](https://docs.papermc.io/paper/dev/getting-started/paper-plugins#loaders) instead of ShadowJars.
- [x] [Gradle](https://github.com/gradle/gradle) with Kotlin DSL
    - [x] Includes [Paperweight](https://github.com/PaperMC/paperweight) for NMS access.
- [x] [CommandAPI](https://github.com/CommandAPI/CommandAPI) for streamlined command creation.
- [x] [Configurate](https://github.com/SpongePowered/Configurate/) for flexible and powerful configuration management.
- [x] CI/CD integration with [GitHub Actions](https://github.com/features/actions)
    - [x] Automated code formatting with [Spotless](.github/workflows/spotless.yml)
- [x] [run-task](https://github.com/jpenilla/run-task) for easily running the development server.
- [x] Ready to use utilities
    - [x] [Logger](./src/main/kotlin/Logger.kt)
- [x] Bootstrapper script ([bootstrap.py](./bootstrap.py)) to quickly customize and initialize your project.
- [ ] Database support using [HikariCP](https://github.com/brettwooldridge/HikariCP)
- [ ] Metrics using [bStats](https://github.com/Bastian/bStats)

## Usage

### Bootstraping

Requirements:

- [Python 3.10+](https://www.python.org/downloads/)
- [Git](https://git-scm.com/downloads) (optional - you can download files directly from GitHub)

```bash
git clone https://github.com/feenko/PaperMC-Kotlin-Template.git PluginName
cd PluginName
python bootstrap.py
```

After running the bootstrapper, you will be asked to fill in some information about your plugin.

### Development

Requirements:

- JDK 21
- [Gradle](https://gradle.org/releases/)

#### Running the development server

```bash
gradle runServer
```

#### Formatting the code

```bash
gradle spotlessApply
```

#### Building the plugin

```bash
gradle build
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Before submitting a pull request, please search for existing issues or create a new one to discuss your proposed changes.
