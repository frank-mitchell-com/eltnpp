README
======

This is an implementation of
a [pull parser](https://en.wikipedia.org/wiki/XML#Pull_parsing)
for [ELTN](http://frank-mitchell.com/projects/eltn/), the author's
"Extended [Lua](https://lua.org) Table Notation".

For an example of one of my previous pull parsers, see
[JSONPP](https://github.com/frank-mitchell-com/jsonpp).

Why a Pull Parser?
------------------

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
a parse tree of Lua values if its future users really want to do that.

What Does It Look Like?
-----------------------

```java
import java.ip.IOException;
import java.io.Reader;
import java.io.StringReader;

import com.frank_mitchell.eltnpp.EltnError;
import com.frank_mitchell.eltnpp.EltnEvent;
import com.frank_mitchell.eltnpp.EltnPullParser;
import com.frank_mitchell.eltnpp.EltnPullParserFactory;

Reader reader = new StringReader(SOME_ELTN_TEXT);

try {
    EltnPullParser parser = EltnPullParserFactory.createParser(reader);
    while (parser.hasNext()) {
        parser.next();
        EltnEvent ev = parser.getEvent();
        switch (ev) {
        case EltnEvent.ERROR:
            System.out.println(ev + " " + parser.getError() + ": " +
                               "[[" + parser.getText() + "]]\n");
            break;
        case EltnEvent.DEF_NAME:
        case EltnEvent.TABLE_KEY_STRING:
        cass EltnEvent.VALUE_STRING:
            System.out.println(ev + " [[" + parser.getString() + "]]\n");
            break;
        case EltnEvent.TABLE_KEY_NUMBER:
        case EltnEvent.TABLE_KEY_INTEGER:
        cass EltnEvent.VALUE_NUMBER:
        case EltnEvent.VALUE_INTEGER:
            System.out.println(ev + " " + parser.getNumber() + "\n");
            break;
        cass EltnEvent.VALUE_TRUE:
        cass EltnEvent.VALUE_FALSE:
        cass EltnEvent.VALUE_NIL:
            System.out.println(ev + " " + parser.getString() + "\n");
            break;
        default:
            System.out.println(ev + " [[" + parser.getText() + "]]\n");
            break;
        }
    }
} catch (IOException e) {
    System.err.println("Caught an exception: " + e.printStackTrace());
} finally {
    reader.close();
}
```

Great! Does It Work?
--------------------

No, not of this writing (2025-05-31).  I'm first checking in an empty skeleton
so I can use Test-Driven Development to write this thing.  I've already
decided on the interfaces; I just have to write the implementation.

In the meantime, read the specification cited above and wait for the C
implementation, which is further along but not ready for prime time yet.
The C API is astonishingly well documented, but the code is a mess, and
it doesn't quite parse let alone emit ELTN at this point.

You can also wait for the Lua implementation which has barely gotten started.
It will probably use the [LPeg](https://www.inf.puc-rio.br/~roberto/lpeg/)
library (or the related [LPegLabel](https://github.com/sqmedeiros/lpeglabel))
which I still don't quite understand.  Maybe some kind soul reading this
will explain it to me.
