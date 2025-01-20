import argparse
from typing import Any, Dict

from bootstrap import *
from bootstrap.cleanup import remove_git_files, remove_github_files, self_destruct


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("-d", "--dry", "--dry-run", action="store_true", dest="dry")
    args = parser.parse_args()

    c = Colors
    print_header(dry_run=args.dry)

    config: Dict[str, Any] = {
        "kotlin_version": get_input(
            "Kotlin version",
            "The Kotlin version to use (2.1.20-Beta1 or 2.1.0)",
            default="2.1.20-Beta1",
            validator=validate_kotlin_version,
        ),
        "group_id": get_input(
            "Group ID",
            "The unique identifier for your project (e.g., com.example)",
            default="com.example",
            validator=validate_group_id,
        ),
        "version": get_input(
            "Version",
            "Your project version (following semantic versioning)",
            default="1.0.0-SNAPSHOT",
            validator=validate_version,
        ),
        "name": get_input(
            "Project name",
            "The name of your project in PascalCase (e.g., MyPlugin)",
            required=True,
            validator=validate_project_name,
        ),
        "description": get_input(
            "Description",
            "A brief description of your plugin (leave empty to remove)",
        ),
        "author": get_input("Author", "Your name or organization", required=True),
    }

    config["kebab_name"] = to_kebab(config["name"])
    config["upper_kebab_name"] = to_kebab(config["name"], upper=True)

    setup = ProjectSetup(dry_run=args.dry)
    setup.update_all(config)

    print(f"{c.GREEN}✓{c.RESET}  Your project is setup and ready to use!")
    print(f"\nNext steps:")
    print(
        f"  {c.BOLD}1){c.RESET} Run {c.BOLD}{c.CYAN}./gradlew build{c.RESET} to build your project"
    )
    print(
        f"  {c.BOLD}2){c.RESET} Find example commands in {c.BOLD}{c.CYAN}ExampleCommand.kt{c.RESET} file"
    )
    print(
        f"  {c.BOLD}3){c.RESET} Configure plugin settings in {c.BOLD}{c.CYAN}paper-plugin.yml{c.RESET} file"
    )
    print(
        f"  {c.BOLD}4){c.RESET} Visit {c.BOLD}{c.CYAN}https://papermc.io/docs{c.RESET} for documentation"
    )

    print(
        f"\n{c.BOLD}{c.GREEN}Thank you for using the PaperMC Kotlin Template!{c.RESET}"
    )

    if not args.dry:
        use_git = get_input(
            "Use Git version control? (Y/n)",
            "Keep Git-specific files (e.g. .gitignore, .gitattributes)",
            default="y",
            validator=validate_yes_no,
        ) in ["y", "yes"]

        if not use_git:
            remove_git_files()
            remove_github_files()
        else:
            use_github = get_input(
                "Use GitHub features? (Y/n)",
                "Keep GitHub-specific files and workflows",
                default="y",
                validator=validate_yes_no,
            ) in ["y", "yes"]

            if not use_github:
                remove_github_files()

        cleanup = get_input(
            "Remove bootstrap files? (y/N)",
            "Clear any footprints of the bootstrap process",
            default="n",
            validator=validate_yes_no,
        ) in ["y", "yes"]

        if cleanup:
            if self_destruct():
                print(f"{c.GREEN}✓{c.RESET}  Bootstrap files removed successfully")
            else:
                print(f"{c.RED}×{c.RESET}  Failed to remove bootstrap files")
                print(f"    You can manually delete them later if needed")
    else:
        print(f"{c.BOLD}{c.GREEN}Dry run completed successfully!{c.RESET}")


if __name__ == "__main__":
    c = Colors
    try:
        main()
    except KeyboardInterrupt:
        print(
            f"\n{c.BOLD}{c.RED}! {c.RESET}{c.RED}Operation cancelled by user{c.RESET}"
        )
        exit(1)
    except Exception as e:
        print(f"\n{c.BOLD}{c.RED}! {c.RESET}{c.RED}{str(e)}{c.RESET}")
        exit(1)
