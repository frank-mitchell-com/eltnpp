- Make build.xml work

- Make the ELTN Pull Parser work:
  - parse atoms (string, number, true, false, nil)
  - parse complex strings
  - parse comments
  - parse top-level "statements"
  - parse tables with name keys
  - parse tables with complex keys

- DON'T parse input until the first call to `next()`.
  The lexer or "Source" this time will throw an exception if called
  before the first call to next().

- hasNext() lookahead?

