package com.javaeedev.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class JsonUtil_MapTest {

    @Test
    public void testEmptyMap2Json() {
        assertEquals("{}", JsonUtil.map2Json(new HashMap<String, Object>()));
    }

    @Test
    public void testSimpleMap2Json() {
        assertEquals("{\"mykey\":\"myvalue\"}", JsonUtil.map2Json(createMap("mykey", "myvalue")));
        assertEquals(
                "{\"mykey1\":\"myvalue1\",\"mykey2\":\"myvalue2\"}",
                JsonUtil.map2Json(
                        createMap(
                                new String[] { "mykey1", "mykey2" },
                                new Object[] { "myvalue1", "myvalue2" }
                        )
                )
        );
        assertEquals(
                "{\"mykey1\":12345,\"mykey2\":true}",
                JsonUtil.map2Json(
                        createMap(
                                new String[] { "mykey1", "mykey2" },
                                new Object[] { 12345, true }
                        )
                )
        );
    }

    @Test
    public void testMapWithArray2Json() {
        assertEquals("{\"mykey\":[1,2,3]}", JsonUtil.map2Json(createMap("mykey", new Integer[] { 1, 2, 3 } )));
        assertEquals(
                "{\"mykey1\":\"myvalue1\",\"mykey2\":[\"A\",\"B\",\"C\"]}",
                JsonUtil.map2Json(
                        createMap(
                                new String[] { "mykey1", "mykey2" },
                                new Object[] { "myvalue1", new String[] { "A", "B", "C" }}
                        )
                )
        );
    }

    @Test
    public void testMapWithMap2Json() {
        Map<String, Object> subMap = createMap("subkey", "subvalue");
        assertEquals("{\"mykey\":{\"subkey\":\"subvalue\"}}", JsonUtil.map2Json(createMap("mykey", subMap )));
    }

    @Test(expected=StackOverflowError.class)
    public void testRecurrsiveMap2Json() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", map);
        JsonUtil.map2Json(map);
    }

    static Map<String, Object> createMap(String key, Object value) {
        return createMap(new String[] { key }, new Object[] { value });
    }

    static Map<String, Object> createMap(String[] keys, Object[] values) {
        if (keys.length!=values.length)
            throw new IllegalArgumentException("Keys and values must have same size.");
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i=0; i<keys.length; i++) {
            String key = keys[i];
            Object value = values[i];
            map.put(key, value);
        }
        return map;
    }
}
