package com.javaeedev.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringUtilTest {

    @Test
    public void testIsEmpty() {
        assertTrue(StringUtil.isEmpty(""));
        assertTrue(StringUtil.isEmpty("  "));
        assertTrue(StringUtil.isEmpty("     "));
    }

    @Test(expected=NullPointerException.class)
    public void testFormatNullLine() {
        StringUtil.formatLine(null);
    }

    @Test
    public void testFormatLine() {
        // empty line:
        assertEquals("", StringUtil.formatLine("  "));
        assertEquals("", StringUtil.formatLine("  \t\t"));
        assertEquals("", StringUtil.formatLine("  " + StringUtil.CHAR_CHINESE_SPACE + "\t"));
        assertEquals("", StringUtil.formatLine(StringUtil.CHAR_CHINESE_SPACE + " \t\t  "));
        // special test:
        assertEquals("\n", StringUtil.formatLine(" \n "));
        // space:
        assertEquals("abc", StringUtil.formatLine(" abc"));
        assertEquals("abc", StringUtil.formatLine("   abc"));
        assertEquals("abc", StringUtil.formatLine("   abc   "));
        assertEquals("abc", StringUtil.formatLine("abc   "));
        assertEquals("abc", StringUtil.formatLine("   abc   "));
        // chinese space:
        assertEquals("abc", StringUtil.formatLine(StringUtil.CHAR_CHINESE_SPACE + "abc"));
        assertEquals("abc", StringUtil.formatLine("   abc " + StringUtil.CHAR_CHINESE_SPACE));
        assertEquals("abc", StringUtil.formatLine(StringUtil.CHAR_CHINESE_SPACE + "   abc " + StringUtil.CHAR_CHINESE_SPACE));
        assertEquals("abc", StringUtil.formatLine(StringUtil.CHAR_CHINESE_SPACE + " \t abc \t " + StringUtil.CHAR_CHINESE_SPACE));
        assertEquals("abc xyz", StringUtil.formatLine(StringUtil.CHAR_CHINESE_SPACE + "abc xyz" + StringUtil.CHAR_CHINESE_SPACE + "\t\t"));
    }

    @Test
    public void testFormatLines() {
        assertEquals("", StringUtil.formatLines("", false));
        assertEquals("", StringUtil.formatLines("  \n\n  ", false));
        assertEquals("", StringUtil.formatLines("  \r  \n  ", false));
        assertEquals("<p></p>", StringUtil.formatLines("", true));
        assertEquals("<p></p>", StringUtil.formatLines("  \n\n  ", true));
        assertEquals("<p></p>", StringUtil.formatLines("  \r  \n  ", true));
        // start with empty:
        assertEquals("abc\nxyz\n", StringUtil.formatLines("  \n \r  abc  \n  xyz ", false));
        assertEquals("<p>abc</p><p>xyz</p>", StringUtil.formatLines("  \n \r  abc  \n  xyz ", true));
        // end with empty:
        assertEquals("abc\nxyz\n", StringUtil.formatLines("  \n \r  abc  \n  xyz \r \n\n  \n", false));
        assertEquals("<p>abc</p><p>xyz</p>", StringUtil.formatLines("  \n \r  abc  \n  xyz \n\n  \r ", true));
        // merge empty lines:
        assertEquals("abc\n\nxyz\n", StringUtil.formatLines("  \n \r  abc  \n \t \r\t\n \r xyz \n  \n", false));
        assertEquals("<p>abc</p><p></p><p>xyz</p>", StringUtil.formatLines("  \n \r  abc  \n  \r\r\n xyz \n\n  \r ", true));
    }

    @Test
    public void testRemoveScriptTags() {
        assertEquals("abcd", StringUtil.removeScriptTags("ab<script src='' language='javascript'>var x;</script>cd"));
        assertEquals("abcd", StringUtil.removeScriptTags("abcd<iframe style='display:hidden' src='hacker.asp'> no end tag!"));
    }

    @Test
    public void testEncodeHtml() {
        assertEquals("&lt;hello&gt;", StringUtil.encodeHtml("<hello>"));
    }

    @Test
    public void testHtml2text() {
        assertEquals("&hello", StringUtil.html2text("&amp;hello"));
        assertEquals("AB", StringUtil.html2text("&#65;&#66;"));
        assertEquals("hello\nworld", StringUtil.html2text("hello<p>world"));
        assertEquals("hello\nworld", StringUtil.html2text("hello<p class='u'>world"));
    }

}
