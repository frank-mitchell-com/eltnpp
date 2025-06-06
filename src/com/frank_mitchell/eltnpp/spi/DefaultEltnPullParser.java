/*
 * The MIT License
 *
 * Copyright 2023 Frank Mitchell.
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

import com.frank_mitchell.codepoint.CodePointSource;
import com.frank_mitchell.eltnpp.EltnError;
import com.frank_mitchell.eltnpp.EltnEvent;
import com.frank_mitchell.eltnpp.EltnPullParser;
import java.io.IOException;

/**
 * Default implementation for {@link EltnPullParser}
 * 
 * @author Frank Mitchell
 */
public class DefaultEltnPullParser implements EltnPullParser {
    private final CodePointSource _source;

    /**
     * Constructor for an instance.
     * Should be used only by an [@link EltnPullParserFactory}.
     * @param source a source of Unicode code points.
     */
    public DefaultEltnPullParser(CodePointSource source) {
        _source = source;
    }

    @Override
    public boolean hasNext() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void next() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EltnEvent getEvent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EltnError getError() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CharSequence getText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getTextOffset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getTextLineNumber() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getTextColumnNumber() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isInTable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Number getNumber() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getDepth() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CharSequence getCurrentKeyText() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EltnEvent getCurrentKeyType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CharSequence getCurrentPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
