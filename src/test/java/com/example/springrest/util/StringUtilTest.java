package com.example.springrest.util;

import com.example.springrest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StringUtilTest {

    @Autowired
    private StringUtil stringUtil;

    @Test
    void trim_success() {
        String value = "poxos ";
        String trim = stringUtil.trim(value);
        assertEquals("poxos", trim);
    }

    @Test
    void trim_null() {
//        assertThrows(NullPointerException.class,
//                ()-> stringUtil.trim(null)
//
//        );
        assertNull(stringUtil.trim(null));
    }

    @Test
    void endsLy_success() {
        String value = "Simply";
        assertTrue(stringUtil.endsLy(value));
    }

    @Test
    void endsLy_false() {
        String value = "Simpl";
        assertFalse(stringUtil.endsLy(value));
    }

    @Test
    void endsLy_null() {
        assertFalse(stringUtil.endsLy(null));
    }

    @Test
    void endsLy_empty() {
        assertFalse(stringUtil.endsLy(""));
    }
}