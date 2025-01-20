import re
import shutil
from pathlib import Path


def remove_git_files() -> None:
    try:
        gitignore = Path(".gitignore")
        if gitignore.exists():
            gitignore.unlink()

        gitattributes = Path(".gitattributes")
        if gitattributes.exists():
            gitattributes.unlink()

        vcs_xml = Path(".idea/vcs.xml")
        if vcs_xml.exists():
            vcs_xml.unlink()
            try:
                vcs_xml.parent.rmdir()
            except OSError:
                pass
    except Exception:
        pass


def remove_github_files() -> None:
    try:
        github_dir = Path(".github")
        if github_dir.exists():
            shutil.rmtree(github_dir)
    except Exception:
        pass


def self_destruct() -> bool:
    try:
        bootstrap_dir = Path("bootstrap")
        if bootstrap_dir.exists():
            shutil.rmtree(bootstrap_dir)

        bootstrap_script = Path("bootstrap.py")
        if bootstrap_script.exists():
            bootstrap_script.unlink()

        readme = Path("README.md")
        if readme.exists():
            readme.unlink()

        assets_dir = Path("assets")
        if assets_dir.exists():
            shutil.rmtree(assets_dir)

        gitignore = Path(".gitignore")
        if gitignore.exists():
            content = gitignore.read_text()
            new_content = re.sub(
                r"# bootstrap \(python\)\n__pycache__/\n+", "", content
            )
            gitignore.write_text(new_content)

        return True

    except Exception:
        return False
