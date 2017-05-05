package com.javaeedev.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class JsonUtil_StringTest {

    private String jsString;
    private String jsonString;

    @Parameters
    @SuppressWarnings("unchecked")
    public static Collection jsStrings2JsonStrings() {
            return Arrays.asList(
                    new Object[][] {
                            // js string -> json string
                            { "",                     "\"\"" },
                            { "abc",                  "\"abc\"" },
                            { "abc\n\rxyz",           "\"abc\\n\\rxyz\"" },
                            { "say \"hello\" to her", "\"say \\\"hello\\\" to her\""  },
                            { "test \\ char",         "\"test \\\\ char\"" },
                            { "test / slash",         "\"test \\/ slash\"" },
                            { "test \f\f",            "\"test \\f\\f\"" },
                            { "test \b back",         "\"test \\b back\"" }
                    }
            );
    }

    public JsonUtil_StringTest(String jsString, String jsonString){
        this.jsString = jsString;
        this.jsonString = jsonString;
    }

    @Test
    public void testString2Json() {
        assertEquals(jsonString, JsonUtil.string2Json(jsString));
    }

}
