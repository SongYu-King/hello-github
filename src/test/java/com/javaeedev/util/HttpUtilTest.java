package com.javaeedev.util;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.*;

public class HttpUtilTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    public void testSetCacheTime() {
        HttpUtil.setCacheTime(response, 0);
        assertEquals("no-cache", response.getHeader("Cache-Control"));
        HttpUtil.setCacheTime(response, -1);
        assertEquals("no-cache", response.getHeader("Cache-Control"));
        HttpUtil.setCacheTime(response, 10);
        assertEquals("max-age=10", response.getHeader("Cache-Control"));
        assertTrue(response.containsHeader("Expires"));
    }

    @Test
    public void testGetURL() {
        request.setQueryString("x=1&y=2");
        request.setRequestURI("/test/a.jsp");
        assertEquals("/test/a.jsp?x=1&y=2", HttpUtil.getURL(request));
    }

    @Test
    public void testGetURLWithNullParameter() {
        request.setRequestURI("/test/a.jsp");
        assertEquals("/test/a.jsp", HttpUtil.getURL(request));
    }

    @Test
    public void testGetInt() {
        request.setParameter("int", "1234567");
        assertEquals(1234567, HttpUtil.getInt(request, "int"));
    }

    @Test
    public void testGetIntWithDefault() {
        assertEquals(1234567, HttpUtil.getInt(request, "int", 1234567));
    }

    @Test(expected=NumberFormatException.class)
    public void testGetIntWithException() {
        request.setParameter("int", "bad");
        HttpUtil.getInt(request, "int");
    }

    @Test
    public void testGetString() {
        request.setParameter("s", "abc");
        assertEquals("abc", HttpUtil.getString(request, "s"));
    }

    @Test
    public void testGetStringWithDefault() {
        request.setParameter("s", "");
        assertEquals("abc", HttpUtil.getString(request, "s", "abc"));
    }

    @Test(expected=NullPointerException.class)
    public void testGetStringWithException() {
        HttpUtil.getString(request, "s");
    }

    @Test
    public void testGetBoolean() {
        request.setParameter("t", "true");
        request.setParameter("f", "false");
        assertTrue(HttpUtil.getBoolean(request, "t"));
        assertFalse(HttpUtil.getBoolean(request, "f"));
        assertFalse(HttpUtil.getBoolean(request, "nonexist"));
    }

    @Test
    public void testGetBooleanWithDefault() {
        assertTrue(HttpUtil.getBoolean(request, "t", true));
        assertFalse(HttpUtil.getBoolean(request, "f", false));
    }

    @Test
    public void testBindForm() {
        request.setParameter("username", "abc");
        request.setParameter("description", "i think 0<1");
        request.setParameter("admin", "true");
        // no "age" parameter!
        request.setParameter("bonus", "999");
        request.setParameter("createdDate", "bad%20parameter");
        // now create form bean:
        TBean bean = new TBean();
        HttpUtil.bindForm(request, bean);
        assertEquals("abc", bean.getUsername());
        assertNull(bean.getPassword());
        assertEquals("i think 0<1", bean.getDescription());
        assertTrue(bean.isAdmin());
        assertEquals(0, bean.getAge());
        assertEquals(999, bean.getBonus());
        assertEquals(0L, bean.getCreatedDate()); // because parameter is invalid
    }

}
