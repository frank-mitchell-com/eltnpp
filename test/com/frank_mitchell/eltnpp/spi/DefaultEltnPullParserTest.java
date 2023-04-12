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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.frank_mitchell.codepoint.CodePointSource;
import com.frank_mitchell.eltnpp.EltnError;
import com.frank_mitchell.eltnpp.EltnEvent;
import com.frank_mitchell.eltnpp.EltnPullParser;
import com.frank_mitchell.eltnpp.EltnPullParserFactory;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author fmitchell
 */
public class DefaultEltnPullParserTest {
    
    private EltnPullParserFactory _factory;
    private EltnPullParser _parser;
    private CodePointSource _source;
    private StringBuilder _builder;

    @BeforeEach
    public void setUp() throws IOException {
        _builder = new StringBuilder();
        _source = new FakeSource(_builder);
        _factory = new DefaultEltnPullParserFactory();
        _parser = _factory.createParser(_source);
    }

    @AfterEach
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
    public void testParseSemicolon() throws IOException {
        push(";");
        
        assertEquals(EltnEvent.START_STREAM, _parser.getEvent());
        assertEquals(EltnError.ERROR_NONE, _parser.isInTable());
        assertFalse(_parser.isInTable());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnEvent.END_STREAM, _parser.getEvent());
        assertEquals(EltnError.ERROR_NONE, _parser.isInTable());
        assertFalse(_parser.isInTable());
        assertFalse(_parser.hasNext());
    }    

    @Test
    public void testParseKeyValue() throws IOException {
        push("key = 1");
        
        assertEquals(EltnEvent.START_STREAM, _parser.getEvent());
        assertEquals(EltnError.ERROR_NONE, _parser.isInTable());
        assertFalse(_parser.isInTable());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnEvent.VAR_NAME, _parser.getEvent());
        assertEquals(EltnError.ERROR_NONE, _parser.isInTable());
        assertFalse(_parser.isInTable());
        assertEquals("key", _parser.getString());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnEvent.VALUE_NUMBER, _parser.getEvent());
        assertEquals(EltnError.ERROR_NONE, _parser.isInTable());
        assertFalse(_parser.isInTable());
        assertEquals(1, _parser.getNumber());
        assertTrue(_parser.hasNext());

        _parser.next();
        assertEquals(EltnEvent.END_STREAM, _parser.getEvent());
        assertEquals(EltnError.ERROR_NONE, _parser.isInTable());
        assertFalse(_parser.isInTable());
        assertFalse(_parser.hasNext());
    }    
}
