# Translation-Library
TOML-based translation library with Python and Java implementation support

## How to run:

You must be in the `Translation-Library` directory for the pathing to work. 

For example, your terminal's absolute path must match:

```bash
/home/user/Projects/Translation-Library
```

## Python 

### Get Translation from File
```bash
$ python src/translator.py [translation_command] [language_name] [variable_name] [category_name] [placeholder_arg1] ... 
```

Accepted translation commands (in the the first argument's place) include: `get-translation`, `translation`, and `translate`

### Example

If in `lib/english.toml`, there is this entry: 
```toml
[gamestart]
welcome="Welcome {name}!"
```

Then the python command might look like:
```bash
$ python src/translator.py get-translation english welcome gamestart name=Blake
```

This would output:
```bash
TRANSLATION: Welcome Blake!
```

### List Supported Languages

```bash
$ python src/translator.py [list_command] [language_name] [variable_name] [category_name] [placeholder_arg1] ... 
```

Accepted translation commands (in the the first argument's place) include: `get-available`, `list`, and `list-available`

### Example

If in `lib/languages.toml`, there are these entries: 
```toml
english="English"
german="Deutsch"
```

Then the python command might look like:
```bash
$ python src/translator.py list
```

This would output:
```bash
OUTPUT: ['English', 'Deutsch']
```

## Java

Documentation coming soon