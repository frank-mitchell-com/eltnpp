TODO
====

Implementation
--------------

- Make the ELTN Pull Parser work:
  - parse atoms (string, number, true, false, nil)
  - parse complex strings
  - parse comments
  - parse top-level "definitions"
  - parse tables with name keys
  - parse tables with complex keys

- DON'T parse input until the first call to `next()`.
  The lexer or "Source" this time will throw an exception if called
  before the first call to next().

- hasNext() lookahead?

Testing
-------

- Escalating series of more complex documents.

- Follow the spec with regard to
  - Identifiers
  - Numbers
  - Strings
  - Long Strings
  - Reserved words

Release
-------

- Implement package protection of Java 11.
- Incude Codepoint in jar?
- Doublecheck JavaDoc.
