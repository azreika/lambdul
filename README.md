# lambdul
A pure lambda calculus interpreter with built-in macro support.

The interpreter will attempt to reduce any lambda expression to its normal form via α-, β-, and η-reductions.

## Setup
Build the interpreter by running `./build` from the base directory.

The interpreter can then be used by running `./lambdul` from the base directory.

## Usage

### Basic Expressions
The interpreter accepts lambda expressions in the following format:

* Abstractions: `\V.(E)`, where `V` is a valid identifier and `Y` is any other lambda expression

* Applications: `(M N)`, where `M` and `N` are any two lambda expressions

* Variables: `V`, where `V` is any valid identifier

A valid identifier is any string satisfying the regex `[a-zA-Z]+` - i.e. any string comprised of one or more letters.

An actual `λ` can be used instead of the backslash `\` in an abstraction.

To reduce the need for brackets, the interpreter automatically assumes:

* left associativity for expressions
    * `(E1 E2 E3 ... EN)` <=> `((...((E1 E2) E3) ...) EN)`
* right associativity for abstractions
    * `\x1.\x2....\xN.E` <=> `(\x1.(\x2.(...(\xN.(E)))))`

### Macros
The interpreter allows the use of macros during a session.

A macro can be assigned with the following syntax:

`M := E`

where `E` is any lambda expression, and `M` is a valid identifier prepended with an underscore (`_`).

For example, we can assign the following expressions to the `_TRUE` and `_FALSE` macros:

```
_TRUE := \x.(\y.x)
_FALSE := \x.(\y.y)
```

The macros can then be used as expressions in any input. For instance, the input

```
((_TRUE \x.x) _FALSE)
```

is now equivalent to the input

```
((\x.(\y.x) \x.x) \x.(\y.y))
```

and will produce the same output:

```
\x.x
```

### Commands
The interpreter supports the use of certain commands, such as:

* `@evaluate EXPR`
    * evaluates the expression `EXPR` in the usual way
    * e.g. `@evaluate (\x.x \x.y)`
* `@assign MACRO EXPR`
    * equivalent to `MACRO := EXPR`
    * e.g. `@assign _NiceExpression ((\x.(\y.x)) \x.x)`
* `@import "FILE_PATH"`
    * evaluates all lines in the specified file at `FILE_PATH`
    * results are not printed, but all environment changes (e.g. assignments) are maintained
    * e.g. `@import "macros.txt"`
* `@exit`
    * exits the interpreter
