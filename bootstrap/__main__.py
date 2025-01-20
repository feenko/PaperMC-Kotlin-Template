import sys
from pathlib import Path

parent_dir = str(Path(__file__).parent.parent)  # add parent directory to path
if parent_dir not in sys.path:
    sys.path.insert(0, parent_dir)

from bootstrap import Colors, main

if __name__ == "__main__":
    c = Colors()
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