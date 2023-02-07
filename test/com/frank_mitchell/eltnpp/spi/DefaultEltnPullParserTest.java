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

import com.frank_mitchell.eltnpp.*;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author fmitchell
 */
public class DefaultEltnPullParserTest {
    
    private EltnPullParserFactory _factory;
    private EltnPullParser _parser;
    private StringBuffer _buffer;
    private 

    @BeforeEach
    public void setUp() throws IOException {
        _factory = new DefaultEltnPullParserFactory();
        _buffer = new StringBuffer("  ");
        _mockSource = new MockSource(_buffer);
        _parser = _factory.createParser(_mockSource);
    }

    @AfterEach
    public void tearDown() {
        _factory = null;
        _parser = null;
        _mockSource = null;
    }

    public void push(CharSequence s) {
        _buffer.append(s);
    }
    
    
}
