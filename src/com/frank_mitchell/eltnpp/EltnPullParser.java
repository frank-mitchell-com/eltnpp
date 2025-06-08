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

/**
 * A pull parser for an ELTN (Extended Lua Table Notation) document.
 *
 * See the
 * <a href="https://frank-mitchell.com/projects/eltn/">ELTN Specification</a>
 * for more information.
 *
 * @author Frank Mitchell
 */
public interface EltnPullParser {
    /**
     * Checks whether the underlying stream has more ELTN elements.
     *
     * @return whether the stream has more ELTN elements.
     * @throws IOException if the character source could not be read.
     */
    public boolean hasNext() throws IOException;

    /**
     * Advances to the next significant ELTN element in the
     * underlying stream.
     *
     * @throws IOException if the character source could not be read.
     */
    public void next() throws IOException;

    /**
     * Get the event parsed by the most recent call to {@link #next()}.
     *
     * @return most recently parsed event.
     */
    public EltnEvent getEvent();

    /**
     * Get code for this error.
     * If {@link getEvent()} is not {@link EltnEvent#ERROR},
     * this method will return {@link EltnError#OK}.
     *
     * @return the current error code, if any.
     */
    public EltnError getError();

    /**
     * Gets the raw text associated with the current event,
     * minus any surrounding whitespace.
     * Every event has associated text, although
     * {@link EltnEvent#TABLE_START} will only return "{",
     * {@link EltnEvent#TABLE_END} will only return "}",
     * and {@link EltnEvent#STREAM_START} and {@link EltnEvent#STREAM_END}
     * will only return "".
     * This can be especially useful on errors.
     *
     * @return text associated with this event.
     */
    public CharSequence getText();

    /**
     * Get the current text's offset in the character stream, if available.
     * This will be a number greater or equal to 0 indicating the number of
     * characters processed before the beginning of the text shown in
     * {@link #getText()}.  Thus the first character is at offset 0.
     *
     * @return an offset &ge; 0, or -1 if not available.
     * @see #getText()
     */
    public int getTextOffset();

    /**
     * Get the current text's line number, if available.
     * This will be a number greater than 0 indicating the number of
     * newline sequences processed before the text shown in
     * {@link #getText()}, plus 1.  Thus the first character is at line 1.
     *
     * @return an offset &gt; 0, or -1 if not available.
     * @see #getText()
     * @see #getTextOffset()
     */
    public int getTextLineNumber();

    /**
     * Get the current text's column number, if available.
     * This will be a number greater than 0 indicating the number of
     * characters processed since the last newline sequence, including
     * the first character of {@link #getText()}, plus 1.
     * Thus the first character in any line is in column 1.
     *
     * @return an offset &gt; 0, or -1 if not available.
     * @see #getText()
     * @see #getTextLineNumber()
     */
    public int getTextColumnNumber();

    /**
     * Indicates if the enclosing value is a ELTN Table.
     *
     * If this object is currently processing the contents of a ELTN Table,
     * this method will return {@code true}.
     *
     * @return {@code true} if the enclosing value is a ELTN Table.
     */
    public boolean isInTable();

    /**
     * Gets the value associated with the current event.
     *
     * On {@link EltnEvent#DEF_NAME},
     * the result is the ELTN string value for the key.
     *
     * On {@link EltnEvent#TABLE_KEY_STRING} or {@link EltnEvent#VALUE_STRING},
     * the result is the ELTN string value with all escape sequences
     * converted to their character values.
     *
     * On {@link EltnEvent#TABLE_KEY_NUMBER}, {@link EltnEvent#VALUE_NUMBER}
     * {@link EltnEvent#TABLE_KEY_INTEGER}, or {@link EltnEvent#VALUE_INTEGER}
     * the result is the string value
     * of the number in its original form (decimal or hexadecimal).
     *
     * On {@link EltnEvent#VALUE_TRUE}, {@link EltnEvent#VALUE_FALSE},
     * or {@link EltnEvent#VALUE_NIL} the result is "true", "false", pr "nil".
     *
     * Otherwise the method throws an exception.
     *
     * @return the string for the current value
     *
     * @throws IllegalStateException if the current event has no string value.
     */
    public String getString();

    /**
     * Gets the numeric value associated with the current event.
     *
     * If {@link #getEvent()} is
     * {@link EltnEvent#TABLE_KEY_INTEGER}, {@link EltnEvent#VALUE_INTEGER},
     * {@link EltnEvent#TABLE_KEY_NUMBER} or {@link EltnEvent#VALUE_NUMBER},
     * this method returns an unspecified subclass of Number.
     * If the current text does not represent a number,
     * this method throws an exception.
     *
     * @return the value of the current ELTN Number
     *
     * @throws java.lang.NumberFormatException if the current text is not a number.
     */
    public Number getNumber() throws NumberFormatException;

    /**
     * Gets a {@code boolean} value for the current event.
     *
     * If {@link #getEvent()} is {@link EltnEvent#VALUE_TRUE} or
     * {@link EltnEvent#VALUE_FALSE}, this method returns the value.
     * If {@link #getEvent()} is {@link EltnEvent#VALUE_NIL},
     * this method returns false.
     * Otherwise this method returns true.
     * This method emulates the convention in Lua that in a Boolean test
     * statement, a value of <em>nil</em> or <em>>false</em> counts as false.
     *
     * @return the Boolean value of the current ELTN object
     */
    default public boolean getBoolean() {
        switch (getEvent()) {
        case VALUE_FALSE:
        case VALUE_NIL:
            return false;
        default:
            return true;
        }
    }

    /**
     * Indicates the depth of nested tables at the current point in the ELTN
     * document. The Definition Table is at depth 0.
     *
     * @return the depth of nested tables.
     */
    public int getDepth();

    /**
     * Gets the key or definition at the current point in the ELTN document.
     * On {@link EltnEvent#DEF_NAME}, {@link EltnEvent#TABLE_KEY_INTEGER},
     * {@link EltnEvent#TABLE_KEY_NUMBER}, or
     * {@link EltnEvent#TABLE_KEY_STRING} this is the current key text.
     * On {@link EltnEvent#TABLE_START}, {@link EltnEvent#VALUE_FALSE},
     * {@link EltnEvent#VALUE_INTEGER}, {@link EltnEvent#VALUE_NIL},
     * {@link EltnEvent#VALUE_NUMBER}, [@link EltnEvent#VALUE_STRING}, or
     * {@link EltnEvent#VALUE_TRUE}, this is the most recently seen key, or
     * the implied key if not preceded by a key or definition name. On
     * {@link EltnEvent#TABLE_END}, this is the key to which the closed
     * table belongs. On other events it is undefined.
     *
     * @return the key or definition name currently being set.
     */
    public CharSequence getCurrentKeyText();

    /**
     * The event type that created {@link #getCurrentKeyText()}. It will be
     * one of {@link EltnEvent#DEF_NAME},
     * {@link EltnEvent#TABLE_KEY_INTEGER}
     * {@link EltnEvent#TABLE_KEY_NUMBER}, or
     * {@link EltnEvent#TABLE_KEY_STRING}.
     *
     * @return the type of the current key.
     * @see #getCurrentKeyText()
     */
    public EltnEvent getCurrentKeyType();

    /**
     * The concatenation of keys or definition names from the top level
     * to the current point in the document. Each element that is an
     * identifier save the first will be preceded by a dot.  Each other
     * element will be enclosed in square brackets and, if a string,
     * single or double quotes, with escape characters marking unprintable
     * characters.
     *
     * @return the path of all current keys.
     */
    public CharSequence getCurrentPath();
}
