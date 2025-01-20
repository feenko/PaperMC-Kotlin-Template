import re
from pathlib import Path
from typing import Any, Dict, List


class ProjectSetup:
    def __init__(self, dry_run: bool = False) -> None:
        self.modified_files: List[str] = []
        self.dry_run: bool = dry_run

    def update_file(self, file: Path, content: str) -> None:
        if self.dry_run:
            self.modified_files.append(file.name)
            return
        file.write_text(content, encoding="utf-8")
        self.modified_files.append(file.name)

    def update_all(self, config: Dict[str, Any]) -> None:
        build_file = Path("build.gradle.kts")
        if build_file.exists():
            content = build_file.read_text(encoding="utf-8")
            content = re.sub(
                r'kotlin\("jvm"\) version "[^"]+"',
                f'kotlin("jvm") version "{config["kotlin_version"]}"',
                content,
            )
            content = re.sub(
                r'group = "[^"]+"', f'group = "{config["group_id"]}"', content
            )
            content = re.sub(
                r'version = "[^"]+"', f'version = "{config["version"]}"', content
            )
            self.update_file(build_file, content)

        settings_file = Path("settings.gradle.kts")
        if settings_file.exists():
            self.update_file(
                settings_file, f'rootProject.name = "{config["kebab_name"]}"'
            )

        plugin_file = Path("src/main/resources/paper-plugin.yml")
        if plugin_file.exists():
            content = plugin_file.read_text(encoding="utf-8")
            pkg_base = f"{config['group_id']}.{config['name'].lower()}"
            content = re.sub(
                r"name: [^\n]+", f'name: {config["upper_kebab_name"]}', content
            )

            if config["description"] is not None and config["description"].strip():
                content = re.sub(
                    r"description: [^\n]+",
                    f'description: {config["description"]}',
                    content,
                )
            else:
                content = re.sub(r"description: [^\n]+\n", "", content)

            content = re.sub(r"author: [^\n]+", f'author: {config["author"]}', content)
            content = re.sub(
                r"main: [^\n]+", f'main: {pkg_base}.{config["name"]}', content
            )
            content = re.sub(
                r"loader: [^\n]+", f'loader: {pkg_base}.{config["name"]}Loader', content
            )
            content = re.sub(
                r"prefix: [^\n]+", f'prefix: {config["name"]}-Kotlin', content
            )
            self.update_file(plugin_file, content)

        src_dir = Path("src/main/kotlin")
        if src_dir.exists():
            old_pkg = "com.example.example"
            new_pkg = f"{config['group_id']}.{config['name'].lower()}"

            for file in src_dir.rglob("*.kt"):
                content = file.read_text(encoding="utf-8")

                if file.name == "ExampleCommand.kt":
                    content = re.sub(
                        f"package {old_pkg}", f"package {new_pkg}", content
                    )
                    content = re.sub(f"import {old_pkg}", f"import {new_pkg}", content)
                else:
                    content = content.replace(old_pkg, new_pkg)
                    if "Example" in content and not "ExampleCommand" in content:
                        content = content.replace("Example", config["name"])

                self.update_file(file, content)

                if (
                    not self.dry_run
                    and "Example" in file.name
                    and file.name != "ExampleCommand.kt"
                ):
                    new_name = file.name.replace("Example", config["name"])
                    file.rename(file.parent / new_name)

        java_src_dir = Path("src/main/java")
        if java_src_dir.exists():
            old_pkg = "com.example.example"
            new_pkg = f"{config['group_id']}.{config['name'].lower()}"
            old_path = java_src_dir / "com" / "example" / "example"
            new_path = java_src_dir

            for part in new_pkg.split("."):
                new_path = new_path / part
                if not self.dry_run:
                    new_path.mkdir(exist_ok=True)

            for file in old_path.rglob("*.java"):
                content = file.read_text(encoding="utf-8")

                content = content.replace(old_pkg, new_pkg)

                if "Example" in content:
                    content = re.sub(r"Example(?!Command\b)", config["name"], content)

                rel_path = file.relative_to(old_path)
                new_file = new_path / rel_path

                if "Example" in new_file.name and not "ExampleCommand" in new_file.name:
                    new_file = new_file.parent / new_file.name.replace(
                        "Example", config["name"]
                    )

                if not self.dry_run:
                    new_file.parent.mkdir(parents=True, exist_ok=True)
                    if file != new_file:
                        file.rename(new_file)

                self.update_file(new_file if not self.dry_run else file, content)

            if not self.dry_run and old_path != new_path:
                try:
                    old_path.rmdir()
                    old_path.parent.rmdir()
                    old_path.parent.parent.rmdir()
                except OSError:
                    pass
