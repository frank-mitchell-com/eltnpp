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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.frank_mitchell.codepoint.CodePointSource;
import com.frank_mitchell.eltnpp.EltnError;
import com.frank_mitchell.eltnpp.EltnEvent;
import com.frank_mitchell.eltnpp.EltnPullParser;
import com.frank_mitchell.eltnpp.EltnPullParserFactory;

/**
 * Tests for a conforming ELTN pull parser.
 * 
 * @author Frank Mitchell
 */
public class EltnPullParserTest {
    
    private EltnPullParserFactory _factory;
    private EltnPullParser _parser;
    private CodePointSource _source;
    private StringBuilder _builder;

    @Before
    public void setUp() throws IOException {
        _builder = new StringBuilder();
        _source = new FakeSource(_builder);
        _factory = new DefaultEltnPullParserFactory();
        _parser = _factory.createParser(_source);
    }

    @After
    public void tearDown() {
        _factory = null;
        _builder = null;
        _source = null;
        _parser = null;
    }
    
    protected void push(CharSequence text) {
        _builder.append(text);
    }

    @Test
    public void testParseEmpty() throws IOException {
        push("");
        
        assertEquals(EltnEvent.STREAM_START, _parser.getEvent());
        assertEquals(EltnError.OK, _parser.isInTable());
        assertFalse(_parser.isInTable());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnEvent.STREAM_END, _parser.getEvent());
        assertEquals(EltnError.OK, _parser.isInTable());
        assertFalse(_parser.isInTable());
        assertFalse(_parser.hasNext());
    }    

    @Test
    public void testParseKeyValue() throws IOException {
        push("key = 1");
        
        assertEquals(EltnEvent.STREAM_START, _parser.getEvent());
        assertEquals(EltnError.OK, _parser.isInTable());
        assertFalse(_parser.isInTable());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnEvent.DEF_NAME, _parser.getEvent());
        assertEquals(EltnError.OK, _parser.isInTable());
        assertFalse(_parser.isInTable());
        assertEquals("key", _parser.getString());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnEvent.VALUE_NUMBER, _parser.getEvent());
        assertEquals(EltnError.OK, _parser.isInTable());
        assertFalse(_parser.isInTable());
        assertEquals(1, _parser.getNumber());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnEvent.STREAM_END, _parser.getEvent());
        assertEquals(EltnError.OK, _parser.isInTable());
        assertFalse(_parser.isInTable());
        assertFalse(_parser.hasNext());
    }    
}
