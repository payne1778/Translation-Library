# Translation Library Project: Python CLI

The Python CLI implementation is still under development and may be under-documented in the code. If you encounter any errors or would like to suggest some feedback, consider opening an issue on GitHub.

For the following feature examples, it is assumed that you are in the `Translation-Library` directory so that the `src/Python/translator.py` path works. However, this script can be ran virtually anywhere, as long as the absolute path of the script is given as a command line argument to your `python` interpreter.

```bash
### Absolute path example
/home/user/Projects/Translation-Library/src/Python/translator.py
```

## Get Translation from File
```bash
$ python src/Python/translator.py [translation_command] [language_name] [variable_name] [section_name] [placeholder_arg1] ...
```

Accepted translation commands (in the the first argument's place) include: `get-translation`, `translation`, and `translate`

### Examples

For the following examples, consider `lib/english.toml`, which has these entries:
```toml
setting = "This is the English language file"
hello = "Hello {name}"

[start]
section_name = "This message is under the start section"
welcome = "Welcome {name}!"
```

## Get message with no section and no placeholder arguments

The python command might look like:
```bash
$ python src/Python/translator.py Translate English setting ""
```

This would output:
```bash
TRANSLATION: This is the English language file
```

## Get message with no section and placeholder arguments

The python command might look like:
```bash
$ python src/Python/translator.py Translate English hello "" name=Blake
```

This would output:
```bash
TRANSLATION: Hello Blake!
```

## Get message under a section and no placeholder arguments

The python command might look like:
```bash
$ python src/Python/translator.py Translate English section_name start
```

This would output:
```bash
TRANSLATION: This message is under the start section
```

## Get message under a section with placeholder arguments

The python command might look like:
```bash
$ python src/Python/translator.py Translate English welcome start name=Blake
```

This would output:
```bash
TRANSLATION: Welcome Blake!
```

### Things to note

- Placeholder arguments are optional, so an empty string (`""`) is not necessary. However, if the desired variable is not under a specific section, an empty string must be passed in.
- The `[translation_command]`, `[language_name]`, and all placeholder argument values are not case sensitive (like `Translate` and `English` and `Blake` for `name=Blake`, respectively).
- The `[variable_name]`, `[section_name]`, and all placeholder argument keys (like `welcome` and `start` and `name` for `name=Blake`, respectively) ARE case sensitive. If any case-sensitive input is incorrect, the script will raise an error.

## List Supported Languages

```bash
$ python src/Python/translator.py [list_command]
```

Accepted translation commands (in the the first argument's place) include: `get-available`, `list`, and `list-available`

### Example

If in `lib/languages.toml`, there are these entries:
```toml
english = "English"
german = "Deutsch"
```

The python command might look like:
```bash
$ python src/Python/translator.py list
```

This would output:
```bash
OUTPUT: ['English', 'Deutsch']
```

## List Supported Languages (Anglicized)

```bash
$ python src/Python/translator.py [list_anglicized_command]
```

Accepted translation commands (in the the first argument's place) include: `get-anglicized-list`, `list-anglicized`, `anglicized-list`

### Example

If in `lib/languages.toml`, there are these entries:
```toml
english = "English"
german = "Deutsch"
```

The python command might look like:
```bash
$ python src/Python/translator.py list-anglicized
```

This would output:
```bash
OUTPUT: ['english', 'german']
```

## Check if a Language is Supported

```bash
$ python src/Python/translator.py [is_supported_command] [language_name]
```

Accepted translation commands (in the the first argument's place) include: `is-supported`, `is-available`, `check-supported`

### Examples

Consider in `lib/languages.toml`, there are these entries:
```toml
english = "English"
german = "Deutsch"
```

### The Language is Supported

The python command might look like:
```bash
$ python src/Python/translator.py is-supported English
```

This would output:
```bash
OUTPUT: True
```

### The Language is Not Supported

The python command might look like:
```bash
$ python src/Python/translator.py is-supported Polish
```

This would output:
```bash
OUTPUT: False
```
