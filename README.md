# Stationeers IC10 Assembler

This project is a simple assembler/preprocessor for the IC10 MIPS language of the game Stationeers.

It is not a new language or a fully fledged assembler, 
it adds a few instructions to handle templating and code splitting.

## Prerequisites
- Java 11 or higher is required to run the assembler (a native executable is planned but not yet available).

For development requirements check the [development section](#development).

## Usage
The assembler is a command line tool that takes in a source file. 
Use the provided script to run it (.sh for linux/mac, .bat for windows).

```bash 
  ic10-assembler.sh <source-file>
```
This will create a directory named `out` which contains an assembled `.ic10` file. 
To get a full explanation of used file types check the [assembly section](#assembly).

### Options
The assembler supports the following command line options:
```shell
Usage: ic10assembler [<options>] <input-file>

Options:
  --version                Show the version and exit
  -o, --output=<path>      Output directory
  -p, --parameter=<value>  Parameter in the form key=value similar to the .DEFINE assembly instruction
  -h, --help               Show this message and exit

Arguments:
  <input-file>  Input file to process
```

## Instructions
The assembler uses a few special instructions. 
All start with a dot (.), are UPPERCASE and must be alone on a line.

### .COMMENT \<text>
comment for assembly only, removed in final output

### .INCLUDE \<relative-path>
includes a different file by relative path to the current file

### .DEFINE \<name> \<value>
defines a constant to be used in the template section

## Templating
Template using predefined values either via the cli-option `-p, --parameter` or .DEFINE

`${<name>}` is replaced by the value of `<name>`

e.g:
```
.DEFINE register r1
mov r0 ${register}
```

results in
```
mov r0 r1
```

Templates are applied recursively in included files. 
Therefore, a value defined in the main file can be used in an included file.

For example:
```
# main.ic10
.DEFINE myname world
.INCLUDE included.ic10

# included.ic10
Hello ${myname}
```

results in
```
Hello world
```

> [!IMPORTANT]
> DEFINES are processed before the templating is applied and only the last value defined is used. 
> So code like below will not give the expected result!
> ```
> .DEFINE myname world
> Hello ${myname}
> .DEFINE myname everyone
> Hello ${myname}
> ```

## Development