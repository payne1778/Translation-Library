import sys
import os
import tomllib

def get_toml_dict(toml_path) -> dict: 
    with open(toml_path, "rb") as f:
        return tomllib.load(f)

def populate_translation_vars(args):
    language = args[2].lower() if len(args) != 0 else None
    message = args[3] if len(args)          != 0 else None
    section = args[4] if len(args)          != 0 else None
    message_args = args[5:] if len(args)    != 0 else None

    message_args = (
        {k: v for k, v in (arg.split("=") for arg in message_args)} if message_args else None
    )
    return language, message, section, message_args

def print_message(toml_path, message, section=None, message_args=None) -> None:
    try: 
        message_string = ""
        toml_dict = get_toml_dict(toml_path)

        if not section and not message_args:
            message_string = toml_dict[message]
        elif section and not message_args:
            message_string = toml_dict[section][message]
        elif not section and message_args:
            message_string = toml_dict[message].format(**message_args)
        else:
            message_string = toml_dict[section][message].format(**message_args)
        print("TRANSLATION: " + message_string)
    except KeyError: 
        if not section and not message_args:
            KeyError(
                f"Variable \"{message}\" in \'{toml_path}\' does not exist. " + 
                "Please recheck language files and/or parameter inputs and spelling."
            )
        elif section and not message_args:
            KeyError(
                f"Section \"{section}\" or variable \"{message}\"" +
                f" in \'{toml_path}\' does not exist. " +
                "Please recheck language files and/or parameter inputs and spelling."
            )
        elif not section and message_args:
            KeyError(
                f"Variable \"{message}\" or the variable's args \"{message_args}\"" +
                f" in \'{toml_path}\' either do not exist or could not be inserted. " +
                "Please recheck language files and/or parameter inputs and spelling."
            )
        else:
            KeyError(
                f"Variable \"{message}\" or Section \"{section}\" do not exist " +
                f"or the variable's args \"{message_args}\" in \'{toml_path}\' " + 
                f"could not be inserted. " + 
                "Please recheck language files and/or parameter inputs and spelling."
            )

def print_message_handler(prefered_path, default_path, message, section, message_args):
    try:
        print_message(prefered_path, message, section, message_args)
    except Exception as e1:
        print(
            f"Could not obtain translation from: {prefered_path} due to {e1}. " +
            f"Attempting to obtain translation from: {default_path}. "
        )
        try: 
            print_message(default_path, message, section, message_args)
        except Exception as e2:
            print(f"Could not obtain translation from: {default_path}. due to {e2}. ")
            raise SystemError("FATAL: Translation files could not be loaded")

def get_available_languages(toml_path) -> list:
    return [language for language in get_toml_dict(toml_path).values()]

def is_available_language(toml_path, language) -> bool:
    toml_dict = get_toml_dict(toml_path)
    return language in toml_dict or language in toml_dict.values()

def are_valid_paths(*paths) -> bool:
    return not any(not os.path.exists(path) for path in paths)

def main():
    translation_commands = {"get-translation", "translation", "translate"}
    list_languages_commands = {"get-available", "list", "list-available"}
    
    # Capture command line arguments and store desired task selection 
    args = sys.argv
    selection = args[1].lower()

    # Check if the desired task was to query for a translation
    translation_mode = selection in translation_commands

    # If translation_mode, then capture relevant/nessecary translation args
    language, message, section, message_args = ("", "", "", "")
    if translation_mode:
        language, message, section, message_args = populate_translation_vars(args)

    # Create paths for various TOML files and the current directory 
    current_dir = os.pardir.replace('\\', '/').replace('"', '')
    language_list_path = f"{current_dir}/lib/languages.toml" 
    default_language_path = f"{current_dir}/lib/english.toml"
    prefered_language_path = (
        f"{current_dir}/lib/{language}.toml" 
        if translation_mode else default_language_path
    )

    ### CHECKS ###

    # Check to make sure that all nessecary paths exist
    # This mainly ensures that everything is located where is should be
    if not are_valid_paths(current_dir, language_list_path, 
        default_language_path, prefered_language_path):
            raise AssertionError("FATAL: Comprehensive path assertion failed.")

    # Ensure that a language and desired message to query for were given 
    if not language or not message:
        raise ValueError(
            "FATAL: Language and/or message argument(s) were not given. " +
            f"Language=\"{language}\", Message=\"{message}\"."
        )
    
    # Ensure that the desired language is supported 
    if not is_available_language(language_list_path, language):
        raise NotImplementedError(
            f"FATAL: Language not supported: {language}" +
            "Corroborate your spelling with the relevant TOML file/entry."
        )

    # print_message(default_language_path, "welcome", "start") # TODO: delete 

    match selection:
        case s if s in translation_commands:
            print_message_handler(
                prefered_language_path, default_language_path, message, section, message_args
            )
        case s if s in list_languages_commands: 
            print(
                "OUTPUT: " + language for language in get_available_languages(language_list_path)
            )
        case "help":
            print("figure it out >:(")
        case _:
            raise ValueError(
                "FATAL: Corecheckuld not resolve input for desired task. " +
                "Please recheck this input or use help for more information"
            )
            
main()