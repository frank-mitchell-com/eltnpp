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

/**
 *
 * @author fmitchell
 */
public enum EltnEvent {
    /**
     * Invalid ELTN syntax.
     */
    SYNTAX_ERROR,

    /**
     * Before first ELTN element
     */
    START_STREAM,

    /**
     * Start of ELTN array (<code>[</code>)
     */
    START_TABLE,

    /**
     * End of ELTN array (<code>]</code>)
     */
    END_TABLE,
    
    /**
     * Simple String key of ELTN object member (<i>name</i> <code>=</code>)
     */
    TABLE_KEY_NAME,
    
    /**
     * Start of non-string key in an ELTN table (<i>[</i>)
     */
    TABLE_KEY_START,

    /**
     * Key of ELTN table member (<i>]</i> <code>=</code>)
     */
    TABLE_KEY_END,

    /**
     * ELTN nil (<code>nil</code>)
     */
    VALUE_NIL,

    /**
     * ELTN Boolean true (<code>true</code>)
     */
    VALUE_TRUE,

    /**
     * ELTN Boolean false (<code>false</code>)
     */
    VALUE_FALSE,

    /**
     * ELTN number
     */
    VALUE_NUMBER,

    /**
     * ELTN string (<code>"</code>...<code>"</code>)
     */
    VALUE_STRING,

    /**
     * After last ELTN element
     */
    END_STREAM    
}
