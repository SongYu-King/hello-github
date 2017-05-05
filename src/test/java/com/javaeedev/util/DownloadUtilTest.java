package com.javaeedev.util;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Run this test with a valid Internet connection. NOTE: this .java file is 
 * UTF-8 encoded, please open with correct encoding in IDE, otherwise, test 
 * will fail for Chinese comparison.
 * 
 * @author Xuefeng
 */
public class DownloadUtilTest {

    @Test
    public void testDownloadSina() throws IOException {
        HttpResponse response = DownloadUtil.download("http://www.sina.com.cn/");
        assertTrue(response.isOk());
        assertTrue(response.isText());
        assertEquals("utf-8", response.getContentEncoding().toLowerCase());
        String text = response.getText();
        assertTrue(text.indexOf("新浪首页")>0);
    }

    @Test
    public void testDownloadDouban() throws IOException {
        HttpResponse response = DownloadUtil.download("http://www.douban.com/");
        assertTrue(response.isOk());
        assertTrue(response.isText());
        assertEquals("utf-8", response.getContentEncoding().toLowerCase());
        String text = response.getText();
        assertTrue(text.indexOf("欢迎来到豆瓣")>0);
    }

    @Test
    public void testRedirect() throws IOException {
        HttpResponse response = DownloadUtil.download("http://bbs.csdn.net/");
        assertTrue(response.isRedirect());
        assertEquals("http://community.csdn.net", response.getRedirectUrl());
    }

}
