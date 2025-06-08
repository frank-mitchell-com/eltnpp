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
package com.frank_mitchell.eltnpp.spi;

import com.frank_mitchell.codepoint.CodePointSource;
import com.frank_mitchell.eltnpp.EltnError;
import com.frank_mitchell.eltnpp.EltnEvent;
import com.frank_mitchell.eltnpp.EltnPullParser;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Default implementation for {@link EltnPullParser}
 *
 * @author Frank Mitchell
 */
final class DefaultEltnPullParser implements EltnPullParser {

	private final DefaultEltnLexer _lexer;

	private EltnEvent _event = EltnEvent.STREAM_START;
	private EltnError _error = EltnError.OK;
	private EltnEvent _lastEvent = EltnEvent.STREAM_START;
	private EltnToken _currToken = null;

	/**
	 * Constructor for an instance. Should be used only by an [@link
	 * EltnPullParserFactory}.
	 *
	 * @param source a source of Unicode code points.
	 */
	public DefaultEltnPullParser(CodePointSource source) {
		_lexer = new DefaultEltnLexer(source);
	}

	@Override
	public boolean hasNext() throws IOException {
		return _event != EltnEvent.ERROR && _event != EltnEvent.STREAM_END;
	}

	@Override
	public EltnEvent getEvent() {
		return _event;
	}

	@Override
	public EltnError getError() {
		return _error;
	}

	@Override
	public CharSequence getText() {
		return getTextString();
	}

    private String getTextString() {
        if (_currToken == null) {
            return "";
        }
        return _currToken.text;
    }

	@Override
	public int getTextOffset() {
		if (_currToken == null) {
			return -1;
		}
		return _currToken.offset;
	}

	@Override
	public int getTextLineNumber() {
		if (_currToken == null) {
			return -1;
		}
		return _currToken.line;
	}

	@Override
	public int getTextColumnNumber() {
		if (_currToken == null) {
			return -1;
		}
		return _currToken.column;
	}

	@Override
	public boolean isInTable() {
		return getDepth() > 0;
	}

	@Override
	public String getString() {
		// TODO: cache results for subsequent invocations
		switch(_currToken.type) {
		case TOKEN_QUOTED_STRING:
			return unescapeQuotedString(getTextString());
		case TOKEN_LONG_STRING:
			return unquoteLongString(getTextString());
		case TOKEN_COMMENT:
			return trimComment(getTextString());
		case TOKEN_LONG_COMMENT:
			return trimLongComment(getTextString());
		default:
			return getTextString();
		}
	}

	static String unquoteLongString(CharSequence cs) {
		return cs.toString();
	}

	static String trimComment(CharSequence cs) {
		return cs.toString();
	}

	static String trimLongComment(CharSequence cs) {
		return unquoteLongString(cs.subSequence(2, -1)).toString();
	}

	static String unescapeQuotedString(CharSequence cs) {
		StringBuffer result = new StringBuffer(cs.length());
		int i = 1; 
		while (i < cs.length() - 1) {
			char c = cs.charAt(i);
			if (c != '\\') {
				result.append(c);
				i++;
				continue;
			}
			c = cs.charAt(i);
			switch(c) {
				case 'a':
					result.append((char)0x07);
					break;
				case 'b':
					result.append('\b');
					break;
				case 'f':
					result.append('\f');
					i++;
					break;
				case 'n':
					result.append('\n');
					i++;
					break;
				case 't':
					result.append('\t');
					i++;
					break;
				case 'r':
					result.append('\r');
					i++;
					break;
				case 'v':
					result.append((char)0x0b);
					i++;
					break;
				case 'u':
					try {
						final Pattern unipattern = Pattern.compile("u{(\\p{XDigit}+)}");
						Matcher match = unipattern.matcher(cs.subSequence(i, cs.length()-i));
						String hexdigits = match.group(1);
						int hexvalue = Integer.parseUnsignedInt(hexdigits, 16);
						result.appendCodePoint(hexvalue);
						i += hexdigits.length()+3;
					} catch (NumberFormatException e) {
						// Invalid hexdigits, so just leave them alone.
					}
				case 'x':
					try {
						String hexdigits = cs.subSequence(i, i+2).toString();
						int hexvalue = Integer.parseUnsignedInt(hexdigits, 16);
						result.append((char)hexvalue);
						i+=2;
					} catch (NumberFormatException e) {
						// Invalid hexdigits, so just leave them alone.
					}
					break;
				case 'z':
					i++;
					c = cs.charAt(i);
					while (DefaultEltnLexer.isEltnSpace(c) && i < cs.length() - 1) {
						i++;
						c = cs.charAt(i);
					}
					result.append(c);
					break;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
					try {
						final Pattern octpattern = Pattern.compile("([0-7]{1,3})");
						Matcher match = octpattern.matcher(cs.subSequence(i, cs.length()-i));
						String octdigits = match.group(1);
						int value = Integer.parseUnsignedInt(octdigits, 8);
						result.appendCodePoint(value);
						i += octdigits.length();
					} catch (NumberFormatException e) {
						// Invalid hexdigits, so just leave them alone.
					}
					break;
				default:
					result.append(c);
					i++;
					break;
			}
		}
		return result.toString();
	}

	@Override
	public Number getNumber() throws NumberFormatException {
		return Double.valueOf(getTextString());
	}

	@Override
	public int getDepth() {
		return 0;
	}

	@Override
	public CharSequence getCurrentKeyText() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public EltnEvent getCurrentKeyType() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public CharSequence getCurrentPath() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/* --------------------------- Parser --------------------------------- */

	@Override
	public void next() throws IOException {
		if (!hasNext()) {
			return;
		}

		if (_event != EltnEvent.COMMENT) {
			_lastEvent = _event;
		}

		EltnToken token = _lexer.nextToken();

        if (null == token.type) {
            _event = EltnEvent.ERROR;
            _error = EltnError.UNKNOWN;
            _currToken = token;
            return;
        }
 
		switch (_lastEvent) {
			case STREAM_START:
                handleStreamStart(token);
				break;

			case COMMENT:
				/* expect based on prior non-comment state */
				break;

			case DEF_NAME:
			case TABLE_KEY_STRING:
			case TABLE_KEY_NUMBER:
			case TABLE_KEY_INTEGER:
                handleKeyDefinition(token);
				break;

			case VALUE_FALSE:
			case VALUE_NIL:
			case VALUE_TRUE:
			case VALUE_INTEGER:
			case VALUE_NUMBER:
			case VALUE_STRING:
                handleNewEntryOrClose(token);
				break;

			case TABLE_START:
				/* expect a TABLE_KEY_*, VALUE_*, TABLE_START, or TABLE_END */
				break;

            case TABLE_END:
				/* expect a COMMA, SEMICOLON, or TABLE_END (if depth > 0) */
				break;

            default:
				/* end state; do nothing */
				break;
		}
	}

    private void handleNewEntryOrClose(EltnToken token) {
        /* expect a COMMA (if depth > 0) or SEMICOLON */
        /* expect another TABLE_KEY_*, a VALUE_*. or a TABLE_START */
        /* else if depth > 0 expect a TABLE_END */
        /* else expect a STREAM_END */
        if (token.type == EltnTokenType.TOKEN_END_OF_STREAM) {
            _event = EltnEvent.STREAM_END;
            _currToken = token;
        } else {
            _event = EltnEvent.ERROR;
            _error = EltnError.UNEXPECTED_TOKEN;
            _currToken = token;
        }
    }

    private void handleKeyDefinition(EltnToken token) throws IOException {
        /* expect an EQUALS then a VALUE_* or TABLE_START */
        if (token.type != EltnTokenType.TOKEN_EQUALS) {
            _event = EltnEvent.ERROR;
            _error = EltnError.INVALID_TOKEN;
            _currToken = token;
        } else {
            token = _lexer.nextToken();
            switch (token.type) {
                case TOKEN_QUOTED_STRING:
                case TOKEN_LONG_STRING:
                    _event = EltnEvent.VALUE_STRING;
                    _currToken = token;
                    break;
                case TOKEN_NUMBER:
                    /* TODO: distinguish integers */
                    _event = EltnEvent.VALUE_NUMBER;
                    _currToken = token;
                    break;
                case TOKEN_TRUE:
                    _event = EltnEvent.VALUE_TRUE;
                    _currToken = token;
                    break;
                case TOKEN_FALSE:
                    _event = EltnEvent.VALUE_FALSE;
                    _currToken = token;
                    break;
                case TOKEN_NIL:
                    _event = EltnEvent.VALUE_NIL;
                    _currToken = token;
                    break;
                default:
                    /* TODO: TABLE_OPEN */
                    _event = EltnEvent.ERROR;
                    _error = EltnError.UNEXPECTED_TOKEN;
                    _currToken = token;
                    break;
            }
        }
    }

    private void handleStreamStart(EltnToken token) {
        /* expect DEF_NAME, TABLE_START, or STRvEAM_END */
        switch (token.type) {
            case TOKEN_END_OF_STREAM:
                _event = EltnEvent.STREAM_END;
                _currToken = token;
                break;
            case TOKEN_IDENTIFIER:
                _event = EltnEvent.DEF_NAME;
                _currToken = token;
                break;
            case TOKEN_CURLY_OPEN:
                _event = EltnEvent.TABLE_START;
                _currToken = token;
                break;
            default:
                _event = EltnEvent.ERROR;
                _error = EltnError.INVALID_TOKEN;
                _currToken = token;
                break;
        }
    }
}
