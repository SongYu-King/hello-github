package com.javaeedev.util;

/**
 * Encode text to html.
 * 
 * @author Xuefeng
 */
public class HtmlUtil {

    /**
     * Encode "&lt;" to "&amp;lt", "&gt;" to "&amp;gt;", "\n" to "&lt;/p&gt;&lt;p&gt;".
     */
    public static String encodeHtml(String text) {
        return text.replace("<", "&lt;").replace(">", "&gt;");
    }

    public static String left(String text, int max) {
        if(text.length()<=max)
            return text;
        return text.substring(0, max-3) + "...";
    }

}
