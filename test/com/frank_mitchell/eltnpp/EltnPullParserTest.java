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
package com.frank_mitchell.eltnpp.spi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.frank_mitchell.eltnpp.EltnError;
import com.frank_mitchell.eltnpp.EltnEvent;
import com.frank_mitchell.eltnpp.EltnPullParser;
import com.frank_mitchell.eltnpp.EltnService;
import java.io.Reader;
import java.io.StringReader;

/**
 * Tests for a conforming ELTN pull parser.
 * 
 * @author Frank Mitchell
 */
public class EltnPullParserTest {
    
    private EltnPullParser _parser;

    protected EltnPullParser createParser(Reader reader) throws IOException {
        return EltnService.createPullParser(reader);
    }

    private EltnPullParser createParserForText(CharSequence text) throws IOException {
        return createParser(new StringReader(text.toString()));
    }

    @Test
    public void testParseEmpty() throws IOException {
        _parser = createParserForText("");
        
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.STREAM_START, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.STREAM_END, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertFalse(_parser.hasNext());
    }    

    @Test
    public void testParseKeyValue() throws IOException {
        _parser = createParserForText("key = 1");
        
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.STREAM_START, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.DEF_NAME, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertEquals("key", _parser.getString());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.VALUE_NUMBER, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertEquals(1.0, _parser.getNumber());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.STREAM_END, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertFalse(_parser.hasNext());
    }    
    
    @Test
    public void testParseKeyValueBoolean() throws IOException {
        _parser = createParserForText("key = true");
        
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.STREAM_START, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.DEF_NAME, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertEquals("key", _parser.getString());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.VALUE_TRUE, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertEquals(true, _parser.getBoolean());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.STREAM_END, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertFalse(_parser.hasNext());
    } 

    @Test
    public void testParseKeyValueString() throws IOException {
        _parser = createParserForText("key = \"a quoted string\"");
        
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.STREAM_START, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.DEF_NAME, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertEquals("key", _parser.getString());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.VALUE_STRING, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertEquals("a quoted string", _parser.getString());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.STREAM_END, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertFalse(_parser.hasNext());
    }

    @Test
    public void testSimpleEscapes() throws IOException {
        runStringFormatTest("'\\a\\b\\f\\n\\r\\t\\v\\\\'",
                            "\u0007\b\f\n\r\t\u000B\\");
    }

    @Test
    public void testEscapedNewline() throws IOException {
        runStringFormatTest("'this text has an \\\nescaped newline'",
                            "this text has an \nescaped newline");
    }

    @Test
    public void testBackslashZ() throws IOException {
        runStringFormatTest("'this text has not \\z\n\t    a newline'",
                            "this text has not a newline");
    }

    @Test
    public void testHexEscapes() throws IOException {
        runStringFormatTest("'this text had \\x68\\x65\\x78 escapes\\x2E'",
                            "this text had hex escapes.");
    }

    @Test
    public void testOctalEscapes() throws IOException {
        runStringFormatTest("'this text had octal escapes\\56\\56\\056\\0'",
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
        _parser = createParserForText("key = " + input);
        
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.STREAM_START, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.DEF_NAME, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertEquals("key", _parser.getString());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.VALUE_STRING, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertEquals(expected, _parser.getString());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnError.OK, _parser.getError());
        assertEquals(EltnEvent.STREAM_END, _parser.getEvent());
        assertFalse(_parser.isInTable());
        assertFalse(_parser.hasNext());
    }
}
