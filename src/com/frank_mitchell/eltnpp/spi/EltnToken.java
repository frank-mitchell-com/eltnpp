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
package com.frank_mitchell.eltnpp.spi;

import java.util.Objects;

/**
 *
 * @author Frank Mitchell <me@frank-mitchell.com>
 */
final class EltnToken {

    final EltnTokenType type;
    final String text;
    final int offset;
    final int line;
    final int column;

    EltnToken(EltnTokenType type, CharSequence text, int offset, int line, int col) {
        this.type = type;
        this.text = text.toString();
        this.offset = offset;
        this.line = line;
        this.column = col;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.type);
        hash = 23 * hash + Objects.hashCode(this.text);
        hash = 23 * hash + this.offset;
        hash = 23 * hash + this.line;
        hash = 23 * hash + this.column;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EltnToken other = (EltnToken) obj;
        if (this.offset != other.offset) {
            return false;
        }
        if (this.line != other.line) {
            return false;
        }
        if (this.column != other.column) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        return this.type == other.type;
    }

    @Override
    public String toString() {
        return "EltnToken{" + "type=" + type + ", text=" + text + ", offset=" + offset + ", line=" + line + ", column=" + column + '}';
    }

}
