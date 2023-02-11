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

import com.frank_mitchell.codepoint.CodePointSource;
import com.frank_mitchell.codepoint.spi.ReaderSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Factory for a {@link EltnPullParser}.
 * Each factory creates an ELTN parser for a stream of ELTN text.
 * Text may be ASCII, UTF-8, UTF-16, or an arbitrary encoding.
 * 
 * @author Frank Mitchell
 */
public interface EltnPullParserFactory {
    /**
     * Creates a stream to parse UTF-16 characters.
     * In other words, a stream of Java {@literal char}s.
     * 
     * @param reader
     * @return a parser for the reader
     * @throws IOException 
     */
    default EltnPullParser createParser(Reader reader) throws IOException {
        return createParser(new ReaderSource(reader));
    }
    
    /**
     * Creates a stream to parse UTF-8 characters.
     * @param stream
     * @return
     * @throws IOException 
     */
    default EltnPullParser createUtf8Parser(InputStream stream) throws IOException {
        return createParser(stream, "UTF-8");
    }
    
    default EltnPullParser createParser(InputStream stream, String encoding) 
            throws IOException {
        return createParser(new InputStreamReader(stream, encoding));
    }
    
    EltnPullParser createParser(CodePointSource source) throws IOException;
}
