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

import java.io.IOException;
import java.math.BigDecimal;

/**
 * A pull parser for an ELTN (Extended Lua Table Notation) document.
 * 
 * @author Frank Mitchell
 * 
 * @see https://frank-mitchell.com/projects/eltn/
 * @see https://lua.org/
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
     * Indicates if the enclosing value is a ELTN Table.
     * 
     * If this object is currently processing the contents of a ELTN Table,
     * this method will return {@code true}.
     * 
     * @return {@code true} if the enclosing value is a ELTN Table.
     * 
     * @see #isInObject() 
     */
    public boolean isInTable();
    
    /**
     * Indicates if the current value is a key in an ELTN table.
     *
     * If this object is currently processing the contents of a table key, this
     * method will return {@code true}.
     *
     * @return {@code true} if the enclosing value is a ELTN Table.
     *
     * @see #isInObject()
     */
    public boolean isInKey();

    /**
     * Gets the value associated with the current event.
     * 
     * On {@link EltnEvent#TABLE_KEY_NAME},
     * the result is the ELTN string value for the key.
     * 
     * On {@link EltnEvent#VALUE_STRING},
     * the result is the ELTN string value with all escape sequences 
     * converted to their character values.
     * 
     * On {@link EltnEvent#VALUE_NUMBER}, the result is the string value
     * of the number in its original form (decimal or hexadecimal).
     * 
     * On {@link EltnEvent#VALUE_TRUE},
     * {@link EltnEvent#VALUE_FALSE},
     * or {@link EltnEvent#VALUE_NIL},
     * the result is "true", "false", or "nil", respectively.
     * 
     * Otherwise the method throws an exception
     * 
     * @return  the string for the current value 
     * 
     * @throws IllegalStateException if the current event has no string value.
     */
    public String getString();

    /**
     * Gets the {@link BigDecimal} value associated with the current event.
     * 
     * If {@link #getEvent()} is {@link EltnEvent#VALUE_NUMBER},
     * this method returns an unspecified subclass of Number.
     * Otherwise this method throws an exception. 
     * 
     * @return the value of the current ELTN Number
     * 
     * @throws IllegalStateException if the current event is not a number.
     */
    public Number getNumber();
    

    /**
     * Gets the {@code double} value associated with the current event.
     * 
     * If {@link #getEvent()} is {@link EltnEvent#VALUE_NUMBER},
     * this method returns an unspecified subclass of Number.
     * Otherwise this method throws an exception. 
     * 
     * @return the value of the current ELTN Number
     * 
     * @throws IllegalStateException if the current event is not a number.
     */
    default public double getDouble() throws IllegalStateException {
        Number n = getNumber();
        if (n == null) {
            throw new IllegalStateException("!" + EltnEvent.VALUE_NUMBER);
        }
        return n.doubleValue();
    }
    
    /**
     * Gets the {@code int} value associated with the current event.
     * 
     * If {@link #getEvent()} is {@link EltnEvent#VALUE_NUMBER},
     * this method returns an unspecified subclass of Number.
     * Otherwise this method throws an exception. 
     * 
     * @return the value of the current ELTN Number
     * 
     * @throws IllegalStateException if the current event is not a number.
     */
    default public int getInt() throws IllegalStateException {
        Number n = getNumber();
        if (n == null) {
            throw new IllegalStateException("!" + EltnEvent.VALUE_NUMBER);
        }
        return n.intValue();
    }
    /**
     * Gets the {@code long} value associated with the current event.
     * 
     * If {@link #getEvent()} is {@link EltnEvent#VALUE_NUMBER},
     * this method returns an unspecified subclass of Number.
     * Otherwise this method throws an exception. 
     * 
     * @return the value of the current ELTN Number
     * 
     * @throws IllegalStateException
     */
    default public long getLong() throws IllegalStateException {
        Number n = getNumber();
        if (n == null) {
            throw new IllegalStateException("!" + EltnEvent.VALUE_NUMBER);
        }
        return n.longValue();
    }

    /**
     * Gets a {@code boolean} value for the current event.
     * 
     * If {@link #getEvent()} is {@link EltnEvent#VALUE_FALSE} or
     * {@link EltnEvent#VALUE_NIL}, this method returns false.
     * Otherwise this method returns true.
     * This method emulates the convention in Lua that in a Boolean test
     * statement, a value of <em>nil</em> or <em>>false</em> counts as false. 
     * 
     * @return the Boolean value of the current ELTN object
     */
    default public boolean getBoolean() throws IllegalStateException {
        EltnEvent event = getEvent();
        return event != EltnEvent.VALUE_NIL && getEvent() != EltnEvent.VALUE_FALSE;
    }
}
