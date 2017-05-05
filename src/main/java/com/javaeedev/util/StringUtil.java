package com.javaeedev.util;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Util for parsing html as pure text for Lucene index, and other function 
 * such as remove all dangerous tags like "script" or "object".
 * 
 * @author Xuefeng
 */
public final class StringUtil {

    private static final String TAG_SCRIPT_START = "<script";
    private static final String TAG_SCRIPT_END = "</script>";

    private static final String TAG_OBJECT_START = "<object";
    private static final String TAG_OBJECT_END = "</object>";

    private static final String TAG_IFRAME_START = "<iframe";
    private static final String TAG_IFRAME_END = "</iframe>";

    public static boolean isEmpty(String s) {
        if(s==null)
            return true;
        return s.trim().length()==0;
    }

    public static final char CHAR_TAB = '\t';
    public static final char CHAR_SPACE = '\u0020';
    public static final char CHAR_CHINESE_SPACE = '\u3000';

    public static String formatTag(String tag) {
        tag = formatLine(tag);
        // combine multiple spaces/tabs into one space:
        StringBuilder sb = new StringBuilder(tag.length());
        boolean lastIsSpace = false;
        for(int i=0; i<tag.length(); i++) {
            char c = tag.charAt(i);
            if(c==CHAR_SPACE || c==CHAR_CHINESE_SPACE || c==CHAR_TAB) {
                if(!lastIsSpace) {
                    lastIsSpace = true;
                    sb.append(CHAR_SPACE);
                }
            }
            else if(c!='\r' && c!='\n') {
                sb.append(c);
                lastIsSpace = false;
            }
        }
        return sb.toString();
    }

    public static String formatLine(String text) {
        return formatLine(text, false);
    }

    public static String formatLine(String text, boolean returnAsHtml) {
        if(text==null)
            throw new NullPointerException("String is null.");
        int start = text.length()-1;
        for(int i=0; i<text.length(); i++) {
            char c = text.charAt(i);
            if(c!=CHAR_SPACE && c!=CHAR_CHINESE_SPACE && c!=CHAR_TAB) {
                start = i;
                break;
            }
        }
        if(start==(text.length()-1))
            return "";
        int end = 0;
        for(int i=text.length()-1; i>=0; i--) {
            char c = text.charAt(i);
            if(c!=CHAR_SPACE && c!=CHAR_CHINESE_SPACE && c!=CHAR_TAB) {
                end = i;
                break;
            }
        }
        String r = text.substring(start, end+1);
        return returnAsHtml ? HtmlUtil.encodeHtml(r) : r;
    }

    public static String formatLines(String text, boolean returnAsHtml) {
        text = text.replace('\r', '\n');
        StringTokenizer tokenizer = new StringTokenizer(text, "\n");
        StringBuilder sb = new StringBuilder(text.length());
        boolean hasText = false;
        boolean lastIsEmpty = true;
        while(tokenizer.hasMoreTokens()) {
            String line = tokenizer.nextToken();
            line = formatLine(line, returnAsHtml);
            if(line.equals("")) {
                lastIsEmpty = true;
            }
            else {
                if(lastIsEmpty) {
                    // decide if append an empty line:
                    if(hasText) {
                        sb.append(returnAsHtml ? "<p></p>" : "\n");
                    }
                    lastIsEmpty = false;
                }
                if(returnAsHtml)
                    sb.append("<p>").append(line).append("</p>");
                else
                    sb.append(line).append('\n');
                hasText = true;
            }
        }
        if(hasText)
            return sb.toString();
        return returnAsHtml ? "<p></p>" : "";
    }

    public static String mid(String s, String startToken, String endToken) {
        return mid(s, startToken, endToken, 0);
    }

    public static String mid(String s, String startToken, String endToken, int fromStart) {
        if (startToken==null || endToken==null)
            return null;
        int start = s.indexOf(startToken, fromStart);
        if (start==(-1))
            return null;
        int end = s.indexOf(endToken, start + startToken.length());
        if (end==(-1))
            return null;
        String sub = s.substring(start + startToken.length(), end);
        return sub.trim();
    }

    /**
     * Compact String by removing space, tab, etc.
     */
    public static String compact(String s) {
        char[] cs = new char[s.length()];
        int len = 0;
        for(int n=0; n<cs.length; n++) {
            char c = s.charAt(n);
            if(c==' ' || c=='\t' || c=='\r' || c=='\n' || c==CHAR_CHINESE_SPACE)
                continue;
            cs[len] = c;
            len++;
        }
        return new String(cs, 0, len);
    }

    public static String removeScriptTags(String html) {
        if(html==null)
            return "";
        boolean found;
        int count = 0;
        int start, end;
        StringBuilder sb = new StringBuilder(html);
        StringBuilder sb_lowercase = new StringBuilder(html.toLowerCase());
        do {
            found = false;
            // remove <script>...</script>
            start = sb_lowercase.lastIndexOf(TAG_SCRIPT_START);
            end = sb_lowercase.lastIndexOf(TAG_SCRIPT_END);
            if(start<end && start>=0) {
                found = true;
                count++;
                sb_lowercase.delete(start, end+9);
                sb.delete(start, end+9);
            }
            if(start>=0 && end==(-1)) {
                found = true;
                count++;
                sb_lowercase.delete(start, sb_lowercase.length());
                sb.delete(start, sb.length());
            }
            else { // remove <object>...</object>
                start = sb_lowercase.lastIndexOf(TAG_OBJECT_START);
                end = sb_lowercase.lastIndexOf(TAG_OBJECT_END);
                if(start<end && start>=0) {
                    found = true;
                    count++;
                    sb_lowercase.delete(start, end+9);
                    sb.delete(start, end+9);
                }
                if(start>=0 && end==(-1)) {
                    found = true;
                    count++;
                    sb_lowercase.delete(start, sb_lowercase.length());
                    sb.delete(start, sb.length());
                }
            }
            // remove <iframe>...</iframe>
            start = sb_lowercase.lastIndexOf(TAG_IFRAME_START);
            end = sb_lowercase.lastIndexOf(TAG_IFRAME_END);
            if(start<end && start>=0) {
                found = true;
                count++;
                sb_lowercase.delete(start, end+9);
                sb.delete(start, end+9);
            }
            if(start>=0 && end==(-1)) {
                found = true;
                count++;
                sb_lowercase.delete(start, sb_lowercase.length());
                sb.delete(start, sb.length());
            }
        } while(found);
        if(count==0) // no tags found, just return the original String!
            return html;
        return sb.toString();
    }

    /**
     * Encode text to html.
     */
    public static String encodeHtml(String html) {
        StringBuilder sb = new StringBuilder(html.length());
        for(int i=0; i<html.length(); i++) {
            char c = html.charAt(i);
            switch(c) {
            case ' ':
                sb.append("&nbsp;");
                break;
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '\n':
                sb.append("<br/>");
                break;
            case '\r':
                break;
            //case '\'':
            //    sb.append("&apos;");
            //    break;
            case '\"':
                sb.append("&quot;");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String html2text(String html) {
        StringBuilder sb = new StringBuilder(html.length());
        char[] data = html.toCharArray();
        int start = 0;
        boolean previousIsPre = false;
        Token token = null;
        for(;;) {
            token = parse(data, start, previousIsPre);
            if(token==null)
                break;
            previousIsPre = token.isPreTag();
            sb = sb.append(token.getText());
            start += token.getLength();
        }
        return sb.toString();
    }

    private static Token parse(char[] data, int start, boolean previousIsPre) {
        if(start>=data.length)
            return null;
        // try to read next char:
        char c = data[start];
        if(c=='<') {
            // this is a tag or comment or script:
            int end_index = indexOf(data, start+1, '>');
            if(end_index==(-1)) {
                // the left is all text!
                return new Token(Token.TOKEN_TEXT, data, start, data.length, previousIsPre);
            }
            String s = new String(data, start, end_index-start+1);
            // now we got s="<...>":
            if(s.startsWith("<!--")) { // this is a comment!
                int end_comment_index = indexOf(data, start+1, "-->");
                if(end_comment_index==(-1)) {
                    // illegal end, but treat as comment:
                    return new Token(Token.TOKEN_COMMENT, data, start, data.length, previousIsPre);
                }
                else
                    return new Token(Token.TOKEN_COMMENT, data, start, end_comment_index+3, previousIsPre);
            }
            String s_lowerCase = s.toLowerCase();
            if(s_lowerCase.startsWith("<script")) { // this is a script:
                int end_script_index = indexOf(data, start+1, "</script>");
                if(end_script_index==(-1))
                    // illegal end, but treat as script:
                    return new Token(Token.TOKEN_SCRIPT, data, start, data.length, previousIsPre);
                else
                    return new Token(Token.TOKEN_SCRIPT, data, start, end_script_index+9, previousIsPre);
            }
            else { // this is a tag:
                return new Token(Token.TOKEN_TAG, data, start, start+s.length(), previousIsPre);
            }
        }
        // this is a text:
        int next_tag_index = indexOf(data, start+1, '<');
        if(next_tag_index==(-1))
            return new Token(Token.TOKEN_TEXT, data, start, data.length, previousIsPre);
        return new Token(Token.TOKEN_TEXT, data, start, next_tag_index, previousIsPre);
    }

    private static int indexOf(char[] data, int start, String s) {
        char[] ss = s.toCharArray();
        // TODO: performance can be improved!
        for(int i=start; i<(data.length-ss.length); i++) {
            // compare from data[i] with ss[0]:
            boolean match = true;
            for(int j=0; j<ss.length; j++) {
                if(data[i+j]!=ss[j]) {
                    match = false;
                    break;
                }
            }
            if(match)
                return i;
        }
        return (-1);
    }

    private static int indexOf(char[] data, int start, char c) {
        for(int i=start; i<data.length; i++) {
            if(data[i]==c)
                return i;
        }
        return (-1);
    }

}

class Token {

    public static final int TOKEN_TEXT    = 0; // html text.
    public static final int TOKEN_COMMENT = 1; // comment like <!-- comments... -->
    public static final int TOKEN_TAG     = 2; // tag like <pre>, <font>, etc.
    public static final int TOKEN_SCRIPT  = 3;

    private static final char[] TAG_BR  = "<br".toCharArray();
    private static final char[] TAG_P   = "<p".toCharArray();
    private static final char[] TAG_LI  = "<li".toCharArray();
    private static final char[] TAG_PRE = "<pre".toCharArray();
    private static final char[] TAG_HR  = "<hr".toCharArray();

    private static final char[] END_TAG_TD = "</td>".toCharArray();
    private static final char[] END_TAG_TR = "</tr>".toCharArray();
    private static final char[] END_TAG_LI = "</li>".toCharArray();

    private static final Map<String, String> SPECIAL_CHARS = new HashMap<String, String>();

    private int type;
    private String html;           // original html
    private String text = null;    // text!
    private int length = 0;        // html length
    private boolean isPre = false; // isPre tag?

    static {
        SPECIAL_CHARS.put("&quot;", "\"");
        SPECIAL_CHARS.put("&lt;",   "<");
        SPECIAL_CHARS.put("&gt;",   ">");
        SPECIAL_CHARS.put("&amp;",  "&");
        SPECIAL_CHARS.put("&reg;",  "(r)");
        SPECIAL_CHARS.put("&copy;", "(c)");
        SPECIAL_CHARS.put("&nbsp;", " ");
        SPECIAL_CHARS.put("&pound;", "?");
        SPECIAL_CHARS.put("&ldquo;", "“");
        SPECIAL_CHARS.put("&rdquo;", "”");
        SPECIAL_CHARS.put("&hellip;", "…");
    }

    public Token(int type, char[] data, int start, int end, boolean previousIsPre) {
        this.type = type;
        this.length = end - start;
        this.html = new String(data, start, length);
        //System.out.println("[Token] html=" + html + ".");
        parseText(previousIsPre);
        //System.out.println("[Token] text=" + text + ".");
    }

    public int getLength() {
        return length;
    }

    public boolean isPreTag() {
        return isPre;
    }

    private void parseText(boolean previousIsPre) {
        if(type==TOKEN_TAG) {
            char[] cs = html.toCharArray();
            if(compareTag(TAG_BR, cs) || compareTag(TAG_P, cs))
                text = "\n";
            else if(compareTag(TAG_LI, cs))
                text = "\n* ";
            else if(compareTag(TAG_PRE, cs))
                isPre = true;
            else if(compareTag(TAG_HR, cs))
                text = "\n--------\n";
            else if(compareString(END_TAG_TD, cs))
                text = "\t";
            else if(compareString(END_TAG_TR, cs) || compareString(END_TAG_LI, cs))
                text = "\n";
        }
        // text token:
        else if(type==TOKEN_TEXT) {
            text = toText(html, previousIsPre);
        }
    }

    public String getText() {
        return text==null ? "" : text;
    }

    private String toText(String html, final boolean isPre) {
        char[] cs = html.toCharArray();
        StringBuilder buffer = new StringBuilder(cs.length);
        int start = 0;
        boolean continueSpace = false;
        char current, next;
        for(;;) {
            if(start>=cs.length)
                break;
            current = cs[start]; // read current char
            if(start+1<cs.length) // and next char
                next = cs[start+1];
            else
                next = '\0';
            if(current==' ') {
                if(isPre || !continueSpace)
                    buffer = buffer.append(' ');
                continueSpace = true;
                // continue loop:
                start++;
                continue;
            }
            // not ' ', so:
            if(current=='\r' && next=='\n') {
                if(isPre)
                    buffer = buffer.append('\n');
                // continue loop:
                start+=2;
                continue;
            }
            if(current=='\n' || current=='\r') {
                if(isPre)
                    buffer = buffer.append('\n');
                // continue loop:
                start++;
                continue;
            }
            // cannot continue space:
            continueSpace = false;
            if(current=='&') {
                // maybe special char:
                int length = readUtil(cs, start, ';', 10);
                if(length==(-1)) { // just '&':
                    buffer = buffer.append('&');
                    // continue loop:
                    start++;
                    continue;
                }
                else { // check if special character:
                    String spec = new String(cs, start, length);
                    String specChar = SPECIAL_CHARS.get(spec);
                    if(specChar!=null) { // special chars!
                        buffer = buffer.append(specChar);
                        // continue loop:
                        start+=length;
                        continue;
                    }
                    else { // check if like '&#1234;':
                        if(next=='#' && cs[start+length-1]==';') { // maybe a char
                            String num = new String(cs, start+2, length-3);
                            try {
                                int code = Integer.parseInt(num);
                                if(code>0 && code<65536) { // this is a special char:
                                    buffer = buffer.append((char)code);
                                    // continue loop:
                                    start+=length;
                                    continue;
                                }
                            }
                            catch(Exception e) {}
                            // just normal char:
                            buffer = buffer.append("&#");
                            // continue loop:
                            start+=2;
                            continue;
                        }
                        else { // just '&':
                            buffer = buffer.append('&');
                            // continue loop:
                            start++;
                            continue;
                        }
                    }
                }
            }
            else { // just a normal char!
                buffer = buffer.append(current);
                // continue loop:
                start++;
                continue;
            }
        }
        return buffer.toString();
    }

    // read from cs[start] util meet the specified char 'util',
    // or null if not found:
    private int readUtil(final char[] cs, final int start, final char util, final int maxLength) {
        int end = start+maxLength;
        if(end>cs.length)
            end = cs.length;
        for(int i=start; i<start+maxLength; i++) {
            if(i<cs.length && cs[i]==util) {
                return i-start+1;
            }
        }
        return (-1);
    }

    // compare standard tag "<input" with tag "<INPUT value=aa>"
    private boolean compareTag(final char[] ori_tag, char[] tag) {
        if(ori_tag.length>=tag.length)
            return false;
        for(int i=0; i<ori_tag.length; i++) {
            if(Character.toLowerCase(tag[i])!=ori_tag[i])
                return false;
        }
        // the following char should not be a-z:
        if(tag.length>ori_tag.length) {
            char c = Character.toLowerCase(tag[ori_tag.length]);
            if(c<'a' || c>'z')
                return true;
            return false;
        }
        return true;
    }

    private boolean compareString(final char[] ori, char[] comp) {
        if(ori.length>comp.length)
            return false;
        for(int i=0; i<ori.length; i++) {
            if(Character.toLowerCase(comp[i])!=ori[i])
                return false;
        }
        return true;
    }

    public String toString() {
        return html;
    }
}

