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

import com.frank_mitchell.codepoint.CodePoint;
import com.frank_mitchell.codepoint.CodePointSource;
import com.frank_mitchell.eltnpp.EltnPullParser;
import com.frank_mitchell.eltnpp.EltnPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Factory object for {@link DefaultEltnPullParser}.
 *
 * @author Frank Mitchell
 */
public class DefaultEltnPullParserFactory implements EltnPullParserFactory {

    /**
     * Default constructor.
     */
    public DefaultEltnPullParserFactory() {
    }

    /**
     * Creates a parser to process UTF-16 characters.
     * In other words, a stream of Java {@code char}s.
     *
     * @param reader a stream of UTF-16 chars.
     * @return a parser for the reader.
     * @throws IOException if the reader throws an exception.
     */
    @Override
    public EltnPullParser createParser(Reader reader) throws IOException {
        return createParser(CodePoint.getSource(reader, StandardCharsets.UTF_16));
    }

    /**
     * Creates a parser to process bytes in the specified encoding.
     *
     * @param stream a stream of bytes.
     * @param cs a character encoding.
     * @return a parser for the stream.
     * @throws IOException if the stream throws an exception.
     */
    @Override
    public EltnPullParser createParser(InputStream stream, Charset cs) throws IOException {
        return createParser(CodePoint.getSource(stream, cs));
    }

    @Override
    public EltnPullParser createParser(CodePointSource source) throws IOException {
        return new DefaultEltnPullParser(source);
    }
}
