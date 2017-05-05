package com.javaeedev.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class HashUtilTest {

    @Parameters
    @SuppressWarnings("unchecked")
    public static Collection parameters() {
        return Arrays.asList(new Object[][] {
                {"abcdefg", "7ac66c0f148de9519b8bd264312c4d64"},
                {"ABCDEFG", "bb747b3df3130fe1ca4afa93fb7d97c9"},
                {"XXXXXXXXXXXXXXXXXXXX", "1cb17f857d5b4bab8268d22f376ce61c"},
                {"yyyyyyyyyyZZZZZZZZZZ", "2d3d65d418a91d80f3410f3394d6f489"},
                {"1234554321", "b0f8b3e58f093359fe1af416b5ea8ed6"},
                {"===== # $$ !!!^_^!!! $$ # =====", "8fe3322c59bfba3f96e692e666d02340"},
                {"md5 is an ONE way encoding algorithm", "b8106f1ca1f6ceeda4836f68685454dd"},
        });
    }

    private String text;
    private String md5;

    public HashUtilTest(String text, String md5) {
        this.text = text;
        this.md5 = md5;
    }

    @Test
    public void testToMD5() {
        assertEquals(md5, HashUtil.toMD5(text));
    }

}
