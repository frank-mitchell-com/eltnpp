/*
 * The MIT License
 *
 * Copyright 2025 Frank Mitchell <me@frank-mitchell.com>.
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

import com.frank_mitchell.codepoint.CodePoint;
import com.frank_mitchell.codepoint.CodePointSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test for the {@link EltnService} object.
 *
 * @author Frank Mitchell <me@frank-mitchell.com>
 */
public class EltnServiceTest {

    @Test
    public void testCreatePullParser_Reader() throws IOException {
        Reader reader = new StringReader("");
        EltnPullParser result = EltnService.createPullParser(reader);
        assertNotNull(result);
    }

    @Test
    public void testCreatePullParser_InputStream_Charset() throws IOException {
        InputStream stream = new ByteArrayInputStream(new byte[0]);
        Charset cs = StandardCharsets.US_ASCII;
        EltnPullParser result = EltnService.createPullParser(stream, cs);
        assertNotNull(result);
    }

    @Test
    public void testCreatePullParser_CodePointSource() throws IOException {
        Reader reader = new StringReader("");
        Charset cs = StandardCharsets.UTF_16;
        CodePointSource cps = CodePoint.getSource(reader, cs);
        EltnPullParser result = EltnService.createPullParser(cps);
        assertNotNull(result);
    }
}
