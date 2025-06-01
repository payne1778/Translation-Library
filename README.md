# Translation-Library
TOML-based translation library with Python and Java implementation support

## Python 

The Python implementation is still under development and may be under-documented in the code. If you encounter any errors or would like to suggest some feedback, consider opening an issue on GitHub. 

For the following feature examples, it is assumed that you are in the `Translation-Library` directory so that the `src/Python/translator.py` path works. However, this script can be ran virtually anywhere, as long as the absolute path of the script is given as a command line argument to your `python` interpreter. 

```bash
# Absolute path example
/home/user/Projects/Translation-Library/src/Python/translator.py 
```

### Get Translation from File
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

### Get message with no section and no placeholder arguments

The python command might look like:
```bash
$ python src/Python/translator.py Translate English setting ""
```

This would output:
```bash
TRANSLATION: This is the English language file
```

### Get message with no section and placeholder arguments

The python command might look like:
```bash
$ python src/Python/translator.py Translate English hello "" name=Blake
```

This would output:
```bash
TRANSLATION: Hello Blake!
```

### Get message under a section and no placeholder arguments

The python command might look like:
```bash
$ python src/Python/translator.py Translate English section_name start
```

This would output:
```bash
TRANSLATION: This message is under the start section
```

### Get message under a section with placeholder arguments

The python command might look like:
```bash
$ python src/Python/translator.py Translate English welcome start name=Blake
```

This would output:
```bash
TRANSLATION: Welcome Blake!
```

### Things to note

- Placeholder arguments are optional, so an empty string (`""`) is not nessacery. However, if the desired variable is not under a specific section, an empty string must be passed in. 
- The `[translation_command]`, `[language_name]`, and all placeholder argument values are not case sensitive (like `Translate` and `English` and `Blake` for `name=Blake`, respectively). 
- The `[variable_name]`, `[section_name]`, and all placeholder argument keys (like `welcome` and `start` and `name` for `name=Blake`, respectively) ARE case sensitive. If any case-senstive input is incorrect, the script will raise an error. 

### List Supported Languages

```bash
$ python src/Python/translator.py [list_command] [language_name] 
```

Accepted translation commands (in the the first argument's place) include: `get-available`, `list`, and `list-available`

### Examples

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

## Java

The Java implementation is still under development and may be under-documented in the code. If you encounter any errors or would like to suggest some feedback, consider opening an issue on GitHub. 

Offical documentation coming soon