package com.github.steveice10.mc.protocol.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author yawkat
 */
public class ObjectUtilTest {
    private static class Test1 {
        String field1 = "abc";
    }

    @Test
    public void testSimple() {
        assertEquals(
                "Test1(field1=abc)",
                ObjectUtil.toString(new Test1())
        );
    }

    private static class Test2 extends Test3 {
        String field1 = "abc";
    }

    private static class Test3 {
        String field2 = "def";
    }

    @Test
    public void testInherit() {
        assertEquals(
                "Test2(field1=abc, field2=def)",
                ObjectUtil.toString(new Test2())
        );
    }

    private static class Test4 {
        int[] field1 = {1, 2, 3};
    }

    @Test
    public void testArray() {
        assertEquals(
                "Test4(field1=[1, 2, 3])",
                ObjectUtil.toString(new Test4())
        );
    }
}