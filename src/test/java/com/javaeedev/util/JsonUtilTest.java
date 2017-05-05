package com.javaeedev.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JsonUtilTest {

    @Test
    public void testToJson() {
        assertEquals("null", JsonUtil.toJson(null));
        final String s = "str \r\n to json!";
        assertEquals(JsonUtil.string2Json(s), JsonUtil.toJson(s));
        final Long l = 123456789L;
        assertEquals(JsonUtil.number2Json(l), JsonUtil.toJson(l));
        final Object[] arr = new Object[] { "string", 12345, 0.5f, true, null };
        assertEquals(JsonUtil.array2Json(arr), JsonUtil.toJson(arr));
    }

    @Test(expected=NullPointerException.class)
    public void testArray2Json() {
        JsonUtil.array2Json(null);
    }

    @Test
    public void testIntArray2Json() {
        assertEquals(
                "[]",
                JsonUtil.intArray2Json(new int[] {})
        );
        assertEquals(
                "[999]",
                JsonUtil.intArray2Json(new int[] {999})
        );
        assertEquals(
                "[1,-2,3,4,99]",
                JsonUtil.intArray2Json(new int[] {1, -2, 3, 4, 99})
        );
    }

    @Test
    public void testLongArray2Json() {
        assertEquals(
                "[]",
                JsonUtil.longArray2Json(new long[] {})
        );
        assertEquals(
                "[999]",
                JsonUtil.longArray2Json(new long[] {999})
        );
        assertEquals(
                "[1,-2,3,4,99]",
                JsonUtil.longArray2Json(new long[] {1, -2, 3, 4, 99})
        );
    }

    @Test
    public void testBooleanArray2Json() {
        assertEquals(
                "[]",
                JsonUtil.booleanArray2Json(new boolean[] {})
        );
        assertEquals(
                "[true]",
                JsonUtil.booleanArray2Json(new boolean[] {true})
        );
        assertEquals(
                "[true,false,true,false,true]",
                JsonUtil.booleanArray2Json(new boolean[] {true, false, true, false, true})
        );
    }

    @Test
    public void testFloatArray2Json() {
        assertEquals(
                "[]",
                JsonUtil.floatArray2Json(new float[] {})
        );
        assertEquals(
                "[99.5]",
                JsonUtil.floatArray2Json(new float[] {99.5f})
        );
        assertEquals(
                "[1.0,2.0,3.0,0.5,1.0E10]",
                JsonUtil.floatArray2Json(new float[] {1, 2, 3, 0.5f, 1e10f})
        );
    }

    @Test
    public void testDoubleArray2Json() {
        assertEquals(
                "[]",
                JsonUtil.doubleArray2Json(new double[] {})
        );
        assertEquals(
                "[99.5]",
                JsonUtil.doubleArray2Json(new double[] {99.5})
        );
        assertEquals(
                "[1.0,2.0,3.0,0.5,1.0E10]",
                JsonUtil.doubleArray2Json(new double[] {1, 2, 3, 0.5f, 1e10})
        );
    }

    @Test
    public void testShortArray2Json() {
        assertEquals(
                "[]",
                JsonUtil.shortArray2Json(new short[] {})
        );
        assertEquals(
                "[999]",
                JsonUtil.shortArray2Json(new short[] {999})
        );
        assertEquals(
                "[1,-2,3,4,99]",
                JsonUtil.shortArray2Json(new short[] {1, -2, 3, 4, 99})
        );
    }

    @Test
    public void testByteArray2Json() {
        assertEquals(
                "[]",
                JsonUtil.byteArray2Json(new byte[] {})
        );
        assertEquals(
                "[127]",
                JsonUtil.byteArray2Json(new byte[] {127})
        );
        assertEquals(
                "[1,-2,3,4,99]",
                JsonUtil.byteArray2Json(new byte[] {1, -2, 3, 4, 99})
        );
    }

    @Test
    public void testBoolean2Json() {
        assertEquals("true", JsonUtil.boolean2Json(Boolean.TRUE));
        assertEquals("false", JsonUtil.boolean2Json(Boolean.FALSE));
    }

    @Test(expected=NullPointerException.class)
    public void testNullBoolean2Json() {
        JsonUtil.boolean2Json(null);
    }

    @Test
    public void testNumber2Json() {
        assertEquals("123", JsonUtil.number2Json(new Byte((byte)123)));
        assertEquals("1234", JsonUtil.number2Json(new Short((short)1234)));
        assertEquals("12345678", JsonUtil.number2Json(new Integer(12345678)));
        assertEquals("123456789000", JsonUtil.number2Json(new Long(123456789000L)));
        assertEquals("-0.5", JsonUtil.number2Json(new Float(-0.5f)));
        assertEquals("1.0E100", JsonUtil.number2Json(new Double(1e100)));
    }

    @Test(expected=NullPointerException.class)
    public void testNullNumber2Json() {
        JsonUtil.number2Json(null);
    }

    @Test(expected=NullPointerException.class)
    public void testString2Json() {
        JsonUtil.string2Json(null);
    }

}
