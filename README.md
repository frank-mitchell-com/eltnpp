This is an implementation of 
a [pull parser](https://en.wikipedia.org/wiki/XML#Pull_parsing)
for [ELTN](http://frank-mitchell/projects/eltn/), the author's
"Extended [Lua](https://lua.org) Table Notation".

For an example of one of my previous pull parsers, see
[JSONPP](https://github.com/frank-mitchell-com/jsonpp).

## Why a Pull Parser?

The following reasons:

1. While regular parsers require a programmer to register callbacks[^if]
   or walk a parse tree, a pull parser keeps the programmer in charge.
   With each invocation the pull parser presents the programmer with 
   the next Lua elements it's found. The programmer can then decide
   how to process each event, pass the parser elsewhere in the program,
   or even abort parsing entirely because the input fails
   application-specific validation.

1. Because they focus on processing the input, pull parsers typically
   consume far less memory than constructing a parse tree, and only
   a little more memory than a comparable callback-based parser.
   (The parser must represent its current state outside the Java call
   stack.)

1. The "table" datatype in Lua encompasses both an associative array
   (which other languages call hashtables, maps, tables, etc.)
   and a sequence (which other languages call lists or arrays).
   Thus it may be difficult to know what data structure a table
   represents without application knowledge.

Future releases may use the pull parser to send callbacks or build
a parse tree of Lua values if they really want to do that.

## What Does It Look Like?

TODO

