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
package com.frank_mitchell.eltnpp;

import com.frank_mitchell.codepoint.CodePointSource;
import com.frank_mitchell.codepoint.CodePoint;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Factory for a {@link EltnPullParser}.
 * Each factory creates an ELTN parser for a stream of ELTN text.
 * Text may be ASCII, UTF-8, UTF-16, or an arbitrary encoding.
 *
 * @author Frank Mitchell
 */
public interface EltnPullParserFactory {
    /**
     * Whether this factory's parsers can handle this particular character
     * set.  Reimplement this method if the EltnPullParsers produced by this
     * object only handle a limited range of character sets, e.g.
     * {@link StandardCharsets#US_ASCII} or {@link StandardCharsets#ISO_8859_1}.
     * 
     * @param cs the Charset for which to test
     * @return whether the parsers process this Charset
     */
    default boolean includesCharset(Charset cs) {
        return true;
    }

    /**
     * Creates a parser to process UTF-16 characters.
     * In other words, a stream of Java {@code char}s.
     *
     * @param reader a stream of UTF-16 chars.
     * @return a parser for the reader.
     * @throws IOException if the reader throws an exception.
     */
    default EltnPullParser createParser(Reader reader) throws IOException {
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
    default EltnPullParser createParser(InputStream stream, Charset cs)
            throws IOException {
        return createParser(CodePoint.getSource(stream, cs));
    }

    /**
     * Creates a parser to process a stream of Unicode code points.
     *
     * @param source a stream of code points.
     * @return a parser for the reader.
     * @throws IOException if the stream throws an exception.
     */
    EltnPullParser createParser(CodePointSource source) throws IOException;
}
