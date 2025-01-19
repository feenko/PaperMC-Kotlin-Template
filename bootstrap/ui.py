import os
import shutil
from typing import Any, Callable, Optional

from .styles import Colors, Styles


def print_header(dry_run: bool = False) -> None:
    os.system("cls" if os.name == "nt" else "clear")

    c, s = Colors, Styles
    title = "PaperMC Kotlin Template"
    version = f"{c.DIM}(Version 1.0){c.RESET}"
    dry_run_text = f"{c.DIM}(--dry-run){c.RESET}" if dry_run else ""
    terminal_width = shutil.get_terminal_size().columns

    print(f"{title} {version} {dry_run_text}")
    print(f"{c.DIM}{s.LIGHT_LINE * terminal_width}{c.RESET}\n")


def get_input(
    prompt: str,
    description: str,
    default: Optional[str] = None,
    required: bool = False,
    validator: Optional[Callable[[str], Any]] = None,
) -> str:
    c = Colors
    print(f"{c.GREEN}◉  {c.RESET}{prompt} {c.GRAY}{description}{c.RESET}")
    prompt_text = f"   {c.GRAY}[{default}]: {c.RESET}"

    while True:
        try:
            value = input(prompt_text).strip()
            if not value and default:
                value = default
            if not value and required:
                print(
                    f"  {c.BOLD}{c.RED}! {c.RESET}{c.RED}This field is required{c.RESET}"
                )
                continue
            if validator and value:
                try:
                    value = validator(value)
                except ValueError as e:
                    print(f"  {c.BOLD}{c.RED}! {c.RESET}{c.RED}{str(e)}{c.RESET}")
                    continue
            print()
            return value
        except KeyboardInterrupt:
            print()
            raise
