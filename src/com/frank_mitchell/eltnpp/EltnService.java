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
import com.frank_mitchell.eltnpp.spi.DefaultEltnPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * Entry point to create an {@link EltnPullParser}.
 *
 * @author Frank Mitchell
 */
public final class EltnService {

    /**
     * Constant for "UTF-32".
     */
    public static final Charset UTF_32 = Charset.forName("UTF-32");

    private EltnService() {
    }

    private static EltnPullParserFactory getParserFactory(Charset cs) {
        EltnPullParserFactory result = null;
        try {
            ServiceLoader<EltnPullParserFactory> loader
                    = ServiceLoader.load(EltnPullParserFactory.class);
            for (EltnPullParserFactory factory : loader) {
                if (factory != null && factory.includesCharset(cs)) {
                    result = factory;
                    break;
                }
            }
        } catch (ServiceConfigurationError e) {
            System.Logger.Level level = System.Logger.Level.WARNING;

            getLogger().log(level, "Error with ServiceLoader; using default", e);
        } finally {
            if (result == null) {
                System.Logger.Level level = System.Logger.Level.TRACE;

                getLogger().log(level, "Using default parser factory");

                result = new DefaultEltnPullParserFactory();
            }
        }
        return result;
    }

    private static System.Logger getLogger() {
        Module module = EltnService.class.getModule();
        System.LoggerFinder loggerFinder = System.LoggerFinder.getLoggerFinder();
        return loggerFinder.getLogger("ELTN", module);
    }

    /**
     * Creates a parser to process UTF-16 characters. In other words, a stream
     * of Java {@code char}s.
     *
     * @param reader a stream of UTF-16 chars.
     *
     * @return a parser for the reader.
     *
     * @throws IOException if the reader throws an exception.
     */
    public static EltnPullParser createPullParser(Reader reader)
            throws IOException {
        EltnPullParserFactory factory
                = getParserFactory(StandardCharsets.UTF_16);
        return factory.createParser(reader);
    }

    /**
     * Creates a parser to process bytes in the specified encoding. Because Java
     * translates strings internally to UTF-16, an ELTN parser in Java cannot
     * remain agnostic about the encoding of its bytes.
     *
     * @param stream a stream of bytes.
     * @param cs a character encoding.
     *
     * @return a parser for the stream.
     *
     * @throws IOException if the stream throws an exception.
     */
    public static EltnPullParser createPullParser(InputStream stream,
            Charset cs) throws IOException {
        EltnPullParserFactory factory = getParserFactory(cs);
        return factory.createParser(stream, cs);
    }

    /**
     * Creates a parser to process bytes in the specified encoding. Because Java
     * translates strings internally to UTF-16, an ELTN parser in Java cannot
     * remain agnostic about the encoding of its bytes.
     *
     * @param cps a stream of Unicode code points.
     *
     * @return a parser for the stream.
     *
     * @throws IOException if the stream throws an exception.
     */
    public static EltnPullParser createPullParser(CodePointSource cps)
            throws IOException {
        EltnPullParserFactory factory = getParserFactory(UTF_32);
        return factory.createParser(cps);
    }
}
