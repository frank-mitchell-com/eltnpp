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

import com.frank_mitchell.codepoint.CodePointSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Collections;
import java.util.Set;

/**
 * Performs lexical analysis on an ELTN document.
 * Its responsibilities begin and end with slicing a stream of characters into
 * discrete tokens using {@link EltnToken} and {@link EltnTokenType}..
 * 
 * @author Frank Mitchell <me@frank-mitchell.com>
 */
class DefaultEltnLexer {

    private static final String[] RESERVED_WORDS_ARRAY = {
        "and", "break", "do", "else", "elseif", "end",
        "false", "for", "function", "global", "goto", "if", "in",
        "local", "nil", "not", "or", "repeat", "return",
        "then", "true", "until", "while"
    };
    
    private static final Set<String> RESERVED_WORDS =
            Collections.unmodifiableSet(new HashSet<>(Arrays.asList(RESERVED_WORDS_ARRAY)));
    
    private static boolean isEltnDigit(int cp) {
        return Character.isDigit(cp);
    }

    private static boolean isEltnNamePart(int cp) {
        return Character.isJavaIdentifierPart(cp);
    }

    private static boolean isEltnNameStart(int cp) {
        return Character.isJavaIdentifierStart(cp);
    }

    private static boolean isEltnNumberPart(int cp) {
        switch(cp) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
            case 'P':
            case 'X':
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
            case 'p':
            case 'x':
            case '+':
            case '-':
            case '.':
                return true;
            default:
                return false;
        }
    }

    static boolean isEltnSpace(int cp) {
        return Character.isWhitespace(cp);
    }

    private int _currentLineNumber = 0;
    private int _currentOffset = 0;
    private boolean _pushback = false;
    private int _currentColumnNumber = 0;
    private final CodePointSource _source;
    
    DefaultEltnLexer(CodePointSource source) {
        _source = source;
    }

    EltnToken nextToken() throws IOException {
        StringBuilder tokenbuf = new StringBuilder();
        EltnTokenType type = EltnTokenType.TOKEN_INVALID;
        /* Skip over whitespace (and comments?) */
        int currentChar = getNextCodePoint();
        while (currentChar >= 0 && isEltnSpace(currentChar)) {
            currentChar = getNextCodePoint();
        }
        int offset = _currentOffset;
        int line = _currentLineNumber;
        int col = _currentColumnNumber;
        if (currentChar >= 0) {
            tokenbuf.appendCodePoint(currentChar);
        }
        switch (currentChar) {
            case -1:
                type = EltnTokenType.TOKEN_END_OF_STREAM;
                break;
            case '}':
                type = EltnTokenType.TOKEN_CURLY_CLOSE;
                break;
            case '{':
                type = EltnTokenType.TOKEN_CURLY_OPEN;
                break;
            case '[':
                /* long quote or '[' */
                currentChar = getNextCodePoint();
                if (currentChar == '[' || currentChar == '=') {
                    tokenbuf.appendCodePoint(currentChar);
                    /* TODO: parse long quote */
                } else {
                    _pushback = true;
                    type = EltnTokenType.TOKEN_SQUARE_OPEN;
                }
                break;
            case ']':
                type = EltnTokenType.TOKEN_SQUARE_CLOSED;
                break;
            case '=':
                type = EltnTokenType.TOKEN_EQUALS;
                break;
            case ',':
                type = EltnTokenType.TOKEN_COMMA;
                break;
            case ';':
                type = EltnTokenType.TOKEN_SEMICOLON;
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '.':
                /* parse number */
                type = parseEltnNumber(tokenbuf);
                break;
            case '-':
                /* number or comment */
                currentChar = getNextCodePoint();
                if (currentChar == '-') {
                    tokenbuf.appendCodePoint(currentChar);
                    /* TODO: parse comment */
                } else if (isEltnDigit(currentChar) || currentChar == '.') {
                    _pushback = true;
                    /* parse negative number */
                    type = parseEltnNumber(tokenbuf);
                }
                break;
            case '"':
            case '\'':
                type = readQuotedString(tokenbuf, currentChar);
                break;
            default:
                if (isEltnNameStart(currentChar)) {
                    /* parse identifier */
                    currentChar = getNextCodePoint();
                    while (isEltnNamePart(currentChar)) {
                        tokenbuf.appendCodePoint(currentChar);
                        currentChar = getNextCodePoint();
                    }
                    if (currentChar >= 0) {
                        _pushback = true;
                    }

                    String identifier = tokenbuf.toString();
                    switch (identifier) {
                        case "false":
                            type = EltnTokenType.TOKEN_FALSE;
                            break;
                        case "nil":
                            type = EltnTokenType.TOKEN_NIL;
                            break;
                        case "true":
                            type = EltnTokenType.TOKEN_TRUE;
                            break;
                        default:
                            if (RESERVED_WORDS.contains(identifier)) {
                                type = EltnTokenType.TOKEN_INVALID;
                            } else {
                                type = EltnTokenType.TOKEN_IDENTIFIER;
                            }
                            break;
                    }
                }
                break;
        }
        return new EltnToken(type, tokenbuf, offset, line, col);
    }

    private EltnTokenType readQuotedString(StringBuilder tokenbuf, int currentChar) throws IOException {
        EltnTokenType type;
        int quoteChar = currentChar;
        int prevChar = currentChar;
        currentChar = getNextCodePoint();
        while (currentChar >= 0 && currentChar <= 0x10FFFF
                && (currentChar != quoteChar || prevChar =='\\')) {
            /* TODO: Stop at unescaped newline */
            tokenbuf.appendCodePoint(currentChar);
            prevChar = currentChar;
            currentChar = getNextCodePoint();
        }
        if (currentChar >= 0 && currentChar <= 0x10FFFF) {
            tokenbuf.appendCodePoint(currentChar);
        }
        type = EltnTokenType.TOKEN_QUOTED_STRING;
        return type;
    }

    private EltnTokenType parseEltnNumber(StringBuilder tokenbuf) throws IOException {
        EltnTokenType type;
        int currentChar;
        currentChar = getNextCodePoint();
        while (isEltnNumberPart(currentChar)) {
            tokenbuf.appendCodePoint(currentChar);
            currentChar = getNextCodePoint();
        }
        if (currentChar >= 0) {
            _pushback = true;
        }
        try {
            /* Cruder than a regular expression, but easier */
            Double d = Double.valueOf(tokenbuf.toString());
            if (!d.isInfinite() && !d.isNaN()) {
                type = EltnTokenType.TOKEN_NUMBER;
            } else {
                type = EltnTokenType.TOKEN_INVALID;
            }
        } catch (NumberFormatException e) {
            type = EltnTokenType.TOKEN_INVALID;
        }
        return type;
    }

    private int getNextCodePoint() throws IOException {
        if (_pushback) {
            _pushback = false;
            return _source.getCodePoint();
        }
        if (!_source.hasNext()) {
            return -1;
        }
        _source.next();
        _currentOffset++;
        if (_source.getCodePoint() == '\n') {
            _currentLineNumber++;
            _currentColumnNumber = 1;
        } else {
            _currentColumnNumber++;
        }
        return _source.getCodePoint();
    }
}
