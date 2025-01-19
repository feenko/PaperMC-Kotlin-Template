# PaperMC Kotlin Template

A modern, feature-rich template for creating Minecraft plugins using PaperMC and Kotlin. This template provides a solid foundation for developing high-performance, maintainable plugins with the latest tools and best practices.

## Features

- [x] Modern [PaperMC](https://papermc.io/) API
- [x] [Gradle](https://gradle.org/) with Kotlin DSL
    - [x] Shadow plugin for creating fat JARs
    - [x] Paperweight for NMS access
- [x] [CommandAPI](https://commandapi.jorel.dev/) for creating commands
- [x] [Okaeri Configs](https://github.com/OkaeriPoland/okaeri-configs) for configuration
- [x] CI/CD with [GitHub Actions](https://github.com/features/actions)
    - [x] Automated code formatting with [Spotless](.github/workflows/spotless.yml)
- [x] [run-task](https://github.com/jpenilla/run-task) for running the development server
- [x] [Bootstraper](./bootstrap.py) for making project your own
- [ ] Database support

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
