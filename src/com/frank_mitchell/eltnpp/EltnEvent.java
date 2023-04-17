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
    SYNTAX_ERROR,

    /**
     * Before first ELTN element
     */
    START_STREAM,

    /**
     * Variable name assignment outside a table (...`=`)
     */
    VAR_NAME,

    /**
     * Start of ELTN array (`[`)
     */
    START_TABLE,

    /**
     * End of ELTN array (`]`)
     */
    END_TABLE,

    /**
     * String key in an ELTN table (...`=` or `[`...`]=`)
     */
    TABLE_KEY_STRING,

    /**
     * Number key in an ELTN table (`[`...`]=`)
     */
    TABLE_KEY_NUMBER,

    /**
     * Boolean key in an ELTN table (`[`...`]=`)
     */
    TABLE_KEY_BOOLEAN,

    /**
     * ELTN nil (`nil`)
     */
    VALUE_NIL,

    /**
     * ELTN Boolean
     */
    VALUE_BOOLEAN,

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
    END_STREAM
}
