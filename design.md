# Assembler

# Steps

## Assembly
Using assembler instructions:

All instructions are case-sensitive and must be alone on a line

### .COMMENT \<text>
comment for assembly only, removed in final output

### .INCLUDE \<relative-path>
includes a different file by relative path to the current file

### .DEFINE \<name> \<value>
defines a constant to be used in the template section

## Templating
Template using predefined values either via cli or .DEFINE

`${<name>}` is replaced by the value of `<name>`

e.g:
```
.DEFINE myname world
Hello ${myname}
```

results in
```
Hello world
```