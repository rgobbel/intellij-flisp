# Femtolisp Syntax

## builtins

### predicates
- binary
  - `eq?`, `eqv?`, `equal?`
- unary
  - `atom?`, `not`, `null?`, `boolean?`, `symbol?`,
     `number?`, `bound?`, `pair?`, `builtin?`, `vector?`, `fixnum?`,
     `function?`

### lists
- `cons` (binary)
- `list` (any number of args)
- `car`, `cdr` (unary)
- `set-car!`, `set-cdr!` (binary)

### execution
- `apply` (at least 2)

### arithmetic
- `+` (any number)
- `-` (at least 1)
- `*` (any number)
- `/` (at least 1 -- with 1 arg, reciprocal)
- `div0`, `=`, `<`, `compare` (binary)

### vectors
- `vector` (any number)
- `aref` (binary)
- `aset!` (3 args)

Any symbol starting or ending with '`:`' is a keyword

### Special forms
 `function`,
  `function:code`,
  `function:vals`,
  `function:env`,
  `function:name`,
  `stacktrace`,
  `gensym`,
  `gensym?`,
  `hash`,
  `copy-list`,
  `append`,
  `list*`,
  `map`,
  `for-each`,

### Other special symbols

 `lambda`, `function`, `quote`, `trycatch`, `quasiquote`, `unquote`,
  `unquote-splicing`, `unquote-nsplicing`, `io-error`, `parse-error`,
  `type-error`, `arg-error`, `unbound-error`, `key-error`, `memory-error`,
  `bounds-error`, `divide-error`, `enumeration-error`

### numbers
  - 0x\<hex digits>, #x\<hex digits> base>15: hex
  - 0\<octal digits>, #o\<octal digits>, base=8: octal
  - \<decimal digits>(.\<decimal digits>)?, #d\<decimal digits>(.\<decimal digits>)?, base=10: decimal
  - \#b\<binary digits>, base=2: binary

### character constants

  - \#\\\<single character>
  - \#\\\<character name>, from: nul, alarm, tab, vtab, return, space, newline
  - \#\\u\<hex digits> (unicode)


