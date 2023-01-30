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

import com.frank_mitchell.eltnpp.EltnEvent;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author fmitchell
 */
public class DefaultEltnPullParserTest {
    
    public DefaultEltnPullParserTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getEvent method, of class DefaultEltnPullParser.
     */
    @Test
    public void testGetEvent() {
        System.out.println("getEvent");
        DefaultEltnPullParser instance = null;
        EltnEvent expResult = null;
        EltnEvent result = instance.getEvent();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isInTable method, of class DefaultEltnPullParser.
     */
    @Test
    public void testIsInTable() {
        System.out.println("isInTable");
        DefaultEltnPullParser instance = null;
        boolean expResult = false;
        boolean result = instance.isInTable();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isInKey method, of class DefaultEltnPullParser.
     */
    @Test
    public void testIsInKey() {
        System.out.println("isInKey");
        DefaultEltnPullParser instance = null;
        boolean expResult = false;
        boolean result = instance.isInKey();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getString method, of class DefaultEltnPullParser.
     */
    @Test
    public void testGetString() {
        System.out.println("getString");
        DefaultEltnPullParser instance = null;
        String expResult = "";
        String result = instance.getString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBigDecimal method, of class DefaultEltnPullParser.
     */
    @Test
    public void testGetBigDecimal() {
        System.out.println("getBigDecimal");
        DefaultEltnPullParser instance = null;
        BigDecimal expResult = null;
        BigDecimal result = instance.getBigDecimal();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDouble method, of class DefaultEltnPullParser.
     */
    @Test
    public void testGetDouble() {
        System.out.println("getDouble");
        DefaultEltnPullParser instance = null;
        double expResult = 0.0;
        double result = instance.getDouble();
        assertEquals(expResult, result, 0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInt method, of class DefaultEltnPullParser.
     */
    @Test
    public void testGetInt() {
        System.out.println("getInt");
        DefaultEltnPullParser instance = null;
        int expResult = 0;
        int result = instance.getInt();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of next method, of class DefaultEltnPullParser.
     */
    @Test
    public void testNext() throws Exception {
        System.out.println("next");
        DefaultEltnPullParser instance = null;
        instance.next();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
