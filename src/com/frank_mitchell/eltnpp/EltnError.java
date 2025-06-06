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
 * Codes for common parse errors.
 *
 * @author Frank Mitchell
 */
public enum EltnError {
    /**
     * No error condition at present.
     */
    OK,

    /**
     * The parser caught an {@link OutOfMemoryError}.
     */
    OUT_OF_MEMORY,

    /**
     * Unexpected end of stream.
     */
    STREAM_END,

    /**
     * A valid ELTN construct in the wrong place.
     */
    UNEXPECTED_TOKEN,

    /**
     * An invalid character or reserved word.
     */
    INVALID_TOKEN,

    /**
     * A key repeated in the same table, explicitly or implicitly.
     * The latter can happen when the implicit index of an entry without
     * a key duplicates an explicit key,or vice versa.
     */
    DUPLICATE_KEY,

    // TODO: other Errors here

    /**
     * Unknown error condition otherwise not enumerated.
     */
    UNKNOWN
}
