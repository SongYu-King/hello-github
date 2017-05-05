package com.javaeedev.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GZipUtilTest {

    @Test
    public void testGzipAndUngzip() {
        final String text = "abcdefg \r\n hijklmn \t opqrst \u3000 uvwxyz";
        byte[] data = text.getBytes();
        byte[] gzipped = GZipUtil.gzip(data);
        byte[] gunzipped = GZipUtil.gunzip(gzipped);
        assertEquals(text, new String(gunzipped));
    }

}
