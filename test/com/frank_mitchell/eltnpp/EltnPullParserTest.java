/*
 * The MIT License
 *
 * Copyright 2023 fmitchell.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.frank_mitchell.eltnpp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

/**
 * Tests for a conforming ELTN pull parser.
 * 
 * @author Frank Mitchell
 */
public class EltnPullParserTest {

    protected EltnPullParser createParser(Reader reader) throws IOException {
        return EltnService.createPullParser(reader);
    }

    private EltnPullParser createParserForText(String text) throws IOException {
        return createParser(new StringReader(text));
    }

    @Test
    public void testParseEmpty() throws IOException {
        EltnPullParser parser = createParserForText("");

        assertStreamStart(parser);

        parser.next();
        assertStreamEnd(parser);
    }

    private void assertStreamStart(EltnPullParser parser) throws IOException {
        assertEquals(EltnError.OK, parser.getError());
        assertEquals(EltnEvent.STREAM_START, parser.getEvent());
        assertFalse(parser.isInTable());
        assertTrue(parser.hasNext());
    }

    private void assertStreamEnd(EltnPullParser parser) throws IOException {
        assertEquals(EltnError.OK, parser.getError());
        assertEquals(EltnEvent.STREAM_END, parser.getEvent());
        assertFalse(parser.isInTable());
        assertFalse(parser.hasNext());
    }

    @Test
    public void testParseKeyValue() throws IOException {
        EltnPullParser parser = createParserForText("key = 1");

        assertStreamStart(parser);

        parser.next();
        assertDefName(parser, "key");

        parser.next();
        assertEvent(parser, EltnEvent.VALUE_NUMBER, "1");
        assertFalse(parser.isInTable());
        assertEquals(1.0, parser.getNumber());

        parser.next();
        assertStreamEnd(parser);
    }

    private void assertDefName(EltnPullParser parser, String name) throws IOException {
        assertEvent(parser, EltnEvent.DEF_NAME, name);
        assertFalse(parser.isInTable());
        assertEquals(name, parser.getString());
    }

    private void assertEvent(EltnPullParser parser, EltnEvent event, CharSequence text)
            throws IOException {
        assertEquals(EltnError.OK, parser.getError());
        assertEquals(event, parser.getEvent());
        assertEquals(text, parser.getText());
        assertTrue(parser.hasNext());
    }

    @Test
    public void testParseKeyValueBoolean() throws IOException {
        EltnPullParser parser = createParserForText("key = true");

        assertStreamStart(parser);

        parser.next();
        assertDefName(parser, "key");

        parser.next();
        assertEvent(parser, EltnEvent.VALUE_TRUE, "true");
        assertFalse(parser.isInTable());
        assertEquals(true, parser.getBoolean());

        parser.next();
        assertStreamEnd(parser);
    }

    @Test
    public void testParseKeyValueString() throws IOException {
        EltnPullParser parser = createParserForText("key = \"a quoted string\"");

        assertStreamStart(parser);

        parser.next();
        assertDefName(parser, "key");

        parser.next();
        assertEvent(parser, EltnEvent.VALUE_STRING, "\"a quoted string\"");
        assertFalse(parser.isInTable());
        assertEquals("a quoted string", parser.getString());

        parser.next();
        assertStreamEnd(parser);
    }

    @Test
    public void testSimpleEscapes() throws IOException {
        runStringFormatTest(
                "'\\a\\b\\f\\n\\r\\t\\v\\\\'",
                "\u0007\b\f\n\r\t\u000B\\");
    }

    @Test
    public void testEscapedNewline() throws IOException {
        runStringFormatTest(
                "'this text has an \\\nescaped newline'",
                "this text has an \nescaped newline");
    }

    @Test
    public void testBackslashZ() throws IOException {
        runStringFormatTest(
                "'this text has not \\z\n\t    a newline'",
                "this text has not a newline");
    }

    @Test
    public void testHexEscapes() throws IOException {
        runStringFormatTest(
                "'this text had \\x68\\x65\\x78 escapes\\x2E'",
                "this text had hex escapes.");
    }

    @Test
    public void testOctalEscapes() throws IOException {
        runStringFormatTest(
                "'this text had octal escapes\\56\\56\\056\\0'",
                "this text had octal escapes...\0");
    }

    @Test
    public void testUnicodeEscapes() throws IOException {
        runStringFormatTest(
                "'this text has unicode:\\u{a}\\u{A9}\\u{1E9E}\\u{1047F}'",
                "this text has unicode:\n\u00A9\u1e9e\ud801\udc7f");
    }

    public void runStringFormatTest(String input, String expected)
            throws IOException {
        EltnPullParser parser = createParserForText("key = " + input);

        assertStreamStart(parser);

        parser.next();
        assertDefName(parser, "key");

        parser.next();
        assertEvent(parser, EltnEvent.VALUE_STRING, input);
        assertFalse(parser.isInTable());
        assertEquals(expected, parser.getString());

        parser.next();
        assertStreamEnd(parser);
    }
}

