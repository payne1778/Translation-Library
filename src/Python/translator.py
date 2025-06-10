from pathlib import Path
import tomllib
import typing
import sys
import os

def get_toml_dict(toml_path: str) -> dict:
    """
    Return a toml file as a dictionary of key-value pairs from a specified
    directory path.

    :param toml_path: The path to the toml file to be loaded
    :return: The entire toml file represented as a dictionary of variable_name
    string keys and their associated message string values
    """
    with open(toml_path, "rb") as f:
        return tomllib.load(f)


def populate_translation_vars(
        args: list[str]
    ) -> str and str and str and dict[str, str]:
    """
    Returns the populated string values of language, variable, section, and
    variable_args for later use in obtaining a translated message.

    :param args: A list of all placeholder args
    :return: `language`, the argument that represents the desired language
    :return: `variable`, the argument that represents the desired variable name
    :return: `section`, the argument that represents the desired variable's
    section name
    :return: `variable_args`, the argument that represents the variable's
    placeholder args
    """
    # Check the length of all args. If the argument is missing, set it to None
    language, variable, section, variable_args = ("", "", "", "")
    try:
        language = args[2].lower() if len(args) != 0 else None
        variable = args[3]         if len(args) != 0 else None
        section = args[4]          if len(args) != 0 else None
        variable_args = args[5:]   if len(args) != 0 else None
    except Exception:
        raise IndexError(
            "FATAL: Missing argument for one of the translation arguments.\n" +
            "Check the following:" +
            f"language_name={language}, " +
            f"variable_name={variable}, " +
            f"section_name={section}, " +
            f"variable_args={variable_args}"
        )
        raise Exception(
            "FATAL: An error occurred while populating translation variables.\n" +
            "Check the following: " +
            f"language_name={language}, " +
            f"variable_name={variable}, " +
            f"section_name={section}, " +
            f"variable_args={variable_args}"
        )

    # Convert variable_args to a dictionary for all of the args in the list
    variable_args = (
        # Example: "name=Blake", adds {"name": "Blake"} to the dictionary
        {k: v for k, v in (arg.split("=") for arg in variable_args)}
        if variable_args else None
    )
    return language, variable, section, variable_args


def print_translated_message(
        toml_path: str,
        variable: str,
        section: str = None,
        variable_args: dict[str, object] = None
    ) -> None:
    """
    Prints the translated message with the given arguments.

    :param toml_path: The path to the toml file to be loaded
    :param variable: The name of the variable in the language toml file that
    contains the variable to be translated
    :param section: The name of the section that the variable might be under
    :param variable_args: A list of all placeholder args to be inserted into
    the message
    """
    try:
        message_string = ""
        toml_dict = get_toml_dict(toml_path)

        if not section and not variable_args:
            message_string = toml_dict[variable]
        elif section and not variable_args:
            message_string = toml_dict[section][variable]
        elif not section and variable_args:
            message_string = toml_dict[variable].format(**variable_args)
        else:
            message_string = toml_dict[section][variable].format(**variable_args)

        print("TRANSLATION: " + message_string)
    except KeyError:
        if not section and not variable_args:
            raise KeyError(
                f"Variable \"{variable}\" in {toml_path} could not be found. " +
                f"(Is \"{variable}\" under a section or not?) " +
                "Please recheck language files, parameter inputs, and spelling."
            )
        elif section and not variable_args:
            raise KeyError(
                f"Section \"{section}\" or variable \"{variable}\" in " +
                f"{toml_path} could not be found. (Is \"{variable}\" under "
                f"\"{section}\"?) " +
                "Please recheck language files, parameter inputs, and spelling."
            )
        elif not section and variable_args:
            raise KeyError(
                f"Variable \"{variable}\" could not be found or the variable\'s " +
                f"args: {variable_args} in {toml_path} could not be inserted. " +
                "Please recheck language files, parameter inputs, and spelling."
            )
        else:
            raise KeyError(
                f"Variable \"{variable}\" or section \"{section}\" could not be" +
                f"found or the variable\'s args {variable_args} in {toml_path}" +
                f"inserted. (Is \"{variable}\" under \"{section}\"?) " +
                "Please recheck language files, parameter inputs, and spelling."
            )


def print_translated_message_handler(
        preferred_path: str,
        default_path: str,
        variable: str,
        section: str,
        variable_args: dict[str, object]
    ) -> None:
    """
    Attempts to first print out the translated message using a preferred
    language. If the preferred language file could not be found or loaded, the
    message will be printed in a default language instead. If this fails, an
    error will be thrown.

    :param preferred_path: The path of the preferred language toml file
    :param default_path: The path of the default language toml file
    :param variable: The name of the variable in the language  toml file that
    contains the variable to be translated
    :param section: The name of the section that the variable might be under
    :param variable_args: A list of all placeholder args to be inserted into
    the message
    """
    try:
        print_translated_message(
            preferred_path, variable, section, variable_args
        )
    except Exception as e1:
        print(
            f"Could not get translation from: {preferred_path} due to {e1}. " +
            f"Attempting to obtain translation from: {default_path}"
        )
        try:
            print_translated_message(
                default_path, variable, section, variable_args
            )
        except Exception as e2:
            print(f"Could not get translation from: {default_path} due to {e2}")
            raise SystemError("FATAL: Translation files could not be loaded")


def get_available_languages(toml_path: str) -> list[str]:
    """
    Returns a list of all supported languages according to the languages toml
    file. Each entry in the list is the language spelled in its local spelling.

    :param toml_path: The path to the languages toml file to be loaded
    :return: A list of all of the supported languages with local spelling
    """
    return [language for language in get_toml_dict(toml_path).values()]


def get_available_languages_anglicized(toml_path: str) -> list[str]:
    """
    Returns a list of all supported languages according to the languages toml
    file. Each entry in the list is the language spelled in its anglicized
    spelling.

    :param toml_path: The path to the languages toml file to be loaded
    :return: A list of all of the supported languages with anglicized spelling
    """
    return [language for language in get_toml_dict(toml_path)]


def is_supported_language(toml_path: str, language: str) -> bool:
    """
    Checks to see if a given language is supported.

    :param toml_path: The path to the languages toml file to be loaded
    :param language: The name of the language to be checked
    :return: `True` if the language is supported, `False` otherwise
    """
    return (
        language.lower() in get_available_languages(toml_path) or
        language in get_available_languages_anglicized(toml_path)
    )


def are_valid_paths(*paths: str) -> list[bool] and bool:
    """
    Checks to see if each of the given paths are valid/exist.

    :param paths: The path strings that are to be checked
    :return: `paths_tested`, a list of booleans that corresponds to each path
    given and the status of whether it was valid or not
    :return: A boolean for whether any of the paths given were invalid
    """
    paths_tested = [os.path.exists(path) for path in paths]
    return paths_tested, not any(False for path in paths_tested)


def help_handler():
    """
    Reads from this file's associated README. Replaces/removes markdown syntax
    in favor of command line friendly symbols and readability.
    """
    with open("./src/Python/README.md", "r") as f:
        file_as_string = f.read()
        file_as_string = (
            file_as_string
            .replace("```bash\n", "")
            .replace("```toml\n", "")
            .replace("```\n", "")
            .replace("###", "--->")
            .replace("##", "->")
            .replace("#", "")
        )
        print(file_as_string)


def main():
    # Initialize all commands as separate lists for easier maintainability
    translation_commands = [
        "get-translation", "translation", "translate"
    ]
    list_languages_commands = [
        "get-available", "list", "list-available", "get-list"
    ]
    list_languages_anglicized_commands = [
        "get-anglicized-list", "list-anglicized", "anglicized-list"
    ]
    is_supported_language_commands = [
        "is-supported", "is-available", "check-supported"
    ]

    # Capture command line arguments and store desired task selection
    args = sys.argv
    selection = args[1].lower()

    # Check if the desired task was a translation query or language support check
    translation_mode = selection in translation_commands
    language_supported_checker_mode = selection in is_supported_language_commands

    # If translation_mode, then capture relevant/necessary translation args
    language, variable, section, variable_args = ("", "", "", "")
    if translation_mode:
        language, variable, section, variable_args = populate_translation_vars(args)

    # If language_supported_checker_mode, then capture the language name to check
    if language_supported_checker_mode:
        language = args[2].lower()

    # Create paths for various TOML files and the project's base directory
    base_directory = str(Path(__file__).resolve().parents[2])
    language_list_path = os.path.join(base_directory, "lib", "languages.toml")
    default_language_path = os.path.join(base_directory, "lib", "english.toml")

    # Use preferred language file if found, otherwise use default language file
    preferred_language_path = (
        os.path.join(base_directory, "lib", f"{language}.toml")
        if translation_mode else default_language_path
    )

    ### CHECKS ###

    # Check to make sure that all necessary paths exist
    # This mainly ensures that everything is located where is should be
    paths_tested, valid_paths = are_valid_paths(
        base_directory,
        language_list_path,
        default_language_path,
        preferred_language_path
    )
    if not valid_paths:
        raise AssertionError(
            "FATAL: Comprehensive path assertion failed. Please check paths: \n" +
            f"base_directory = \'{base_directory}\', " +
                ("PASSED\n" if paths_tested[0] else "FAILED\n") +
            f"language_list_path = \'{language_list_path}\', " +
                ("PASSED\n" if paths_tested[1] else "FAILED\n") +
            f"default_language_path = \'{default_language_path}\', " +
                ("PASSED\n" if paths_tested[2] else "FAILED\n") +
            f"preferred_language_path = \'{preferred_language_path}\', " +
                ("PASSED\n" if paths_tested[3] else "FAILED\n")
        )

    # Ensure that a language and desired variable to query for were given
    if (not language or not variable) and translation_mode:
        raise ValueError(
            "FATAL: Language and/or variable argument(s) were not given. " +
            f"Language=\"{language}\", Variable=\"{variable}\"."
        )

    # Ensure that the desired language is supported
    if (
        translation_mode and
        not is_supported_language(language_list_path, language)
    ):
        raise NotImplementedError(
            f"FATAL: The language \"{language}\" is not supported. " +
            "Corroborate your spelling with the relevant TOML file/entry."
        )

    match selection:
        case s if s in translation_commands:
            print_translated_message_handler(
                preferred_language_path,
                default_language_path,
                variable,
                section,
                variable_args
            )
        case s if s in list_languages_commands:
            print(
                "OUTPUT: " + str(get_available_languages(language_list_path))
            )
        case s if s in list_languages_anglicized_commands:
            print(
                "OUTPUT: " + str(
                    get_available_languages_anglicized(language_list_path)
                )
            )
        case s if s in is_supported_language_commands:
            print(
                "OUTPUT: " + str(
                    is_supported_language(language_list_path, language)
                )
            )
        case "help":
            help_handler()
        case _:
            raise ValueError(
                f"FATAL: Could not determine the desired task: \"{selection}\". " +
                "(Did you spell it correctly and is it a valid command?) " +
                "Please recheck this input or use help for more information"
            )

main()
