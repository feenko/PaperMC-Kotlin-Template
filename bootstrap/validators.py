import re


def validate_group_id(value: str) -> str:
    if not re.match(r"^[a-z][a-z0-9_]*(\.[a-z][a-z0-9_]*)*$", value):
        raise ValueError("Must be lowercase, dot-separated words (e.g., com.example)")
    return value


def validate_version(value: str) -> str:
    if not re.match(r"^\d+\.\d+\.\d+(-[A-Za-z0-9.]+)?$", value):
        raise ValueError("Must follow semver (e.g., 1.0.0 or 1.0.0-SNAPSHOT)")
    return value


def validate_project_name(value: str) -> str:
    if not re.match(r"^[A-Za-z][A-Za-z0-9]*$", value):
        raise ValueError(
            "Must start with a letter and contain only letters and numbers"
        )
    return value


def validate_kotlin_version(value: str) -> str:
    allowed_versions = ["2.1.20-Beta1", "2.1.0"]
    if value not in allowed_versions:
        raise ValueError(f"Must be one of: {', '.join(allowed_versions)}")
    return value


def validate_yes_no(value: str) -> str:
    lowered = value.lower()
    if lowered in ["y", "n", "yes", "no"]:
        return lowered
    raise ValueError("Please enter y/n")


def to_kebab(s: str, upper: bool = False) -> str:
    kebab = re.sub(
        "([a-z0-9])([A-Z])", r"\1-\2", re.sub("([A-Z])([A-Z][a-z])", r"\1-\2", s)
    ).lower()
    return (
        "-".join(word.capitalize() for word in kebab.split("-"))
        if upper
        else kebab.lower()
    )
