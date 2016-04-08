package org.spacehq.mc.protocol.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author yawkat
 */
public class ReflectionToStringTest {
    @Test
    public void testSimple() {
        assertEquals(
                "Test1(field1=abc)",
                ReflectionToString.toString(new Test1())
        );
    }

    private static class Test1 {
        String field1 = "abc";
    }

    @Test
    public void testInherit() {
        assertEquals(
                "Test2(field1=abc, field2=def)",
                ReflectionToString.toString(new Test2())
        );
    }

    private static class Test2 extends Test3 {
        String field1 = "abc";
    }

    private static class Test3 {
        String field2 = "def";
    }

    @Test
    public void testArray() {
        assertEquals(
                "Test4(field1=[1, 2, 3])",
                ReflectionToString.toString(new Test4())
        );
    }

    private static class Test4 {
        int[] field1 = new int[]{ 1, 2, 3 };
    }
}