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

/**
 * Events representing the major semantic elements of an ELTN file. See the 
 * <a href="https://frank-mitchell.com/projects/eltn/">ELTN Specification</a>.
 * 
 * @author Frank Mitchell
 */
public enum EltnEvent {
    /**
     * Invalid ELTN syntax.
     */
    ERROR,

    /**
     * Before first ELTN element
     */
    STREAM_START,

    /**
     * Definition name assignment outside a table (...`=`)
     */
    DEF_NAME,

    /**
     * Start of ELTN array (`[`)
     */
    TABLE_START,

    /**
     * End of ELTN array (`]`)
     */
    TABLE_END,

    /**
     * String key in an ELTN table (...`=` or `[`...`]=`)
     */
    TABLE_KEY_STRING,

    /**
     * Number key in an ELTN table (`[`...`]=`)
     */
    TABLE_KEY_NUMBER,

    /**
     * Integer key in an ELTN table (`[`...`]=`)
     */
    TABLE_KEY_INTEGER,

    /**
     * ELTN nil (`nil`)
     */
    VALUE_NIL,

    /**
     * ELTN Boolean `false`
     */
    VALUE_FALSE,

    /**
     * ELTN Boolean `true`
     */
    VALUE_TRUE,

    /**
     * ELTN integer
     */
    VALUE_INTEGER,

    /**
     * ELTN number
     */
    VALUE_NUMBER,

    /**
     * ELTN string (`"`...`"`)
     */
    VALUE_STRING,

    /**
     * After last ELTN element
     */
    STREAM_END
}
