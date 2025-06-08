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
public final class EltnService {

    /*
     * TODO: should use the Service loader, but for now I'll just kludge
     * it this way.
     */
    private static final EltnPullParserFactory INSTANCE =
        new com.frank_mitchell.eltnpp.spi.DefaultEltnPullParserFactory();

    /**
     * Creates a parser to process UTF-16 characters.
     * In other words, a stream of Java {@code char}s.
     *
     * @param reader a stream of UTF-16 chars.
     * @return a parser for the reader.
     * @throws IOException if the reader throws an exception.
     */
    public static EltnPullParser createPullParser(Reader reader)
            throws IOException {
        return INSTANCE.createParser(reader);
    }

    /**
     * Creates a parser to process bytes in the specified encoding.
     * Because Java translates strings internally to UTF-16, an ELTN
     * parser in Java cannot remain agnostic about the encoding of its
     * bytes.
     *
     * @param stream a stream of bytes.
     * @param cs a character encoding.
     * @return a parser for the stream.
     * @throws IOException if the stream throws an exception.
     */
    public static EltnPullParser createPullParser(InputStream stream,
            Charset cs) throws IOException {
        return INSTANCE.createParser(stream, cs);
    }
}
