package utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

class NumberTest {
    @Test
    void baseConversion() {
        String s = "woaini";
        byte[] bytes = s.getBytes();
        System.out.println(Number.baseConversion(bytes, 2));
    }

    @Test
    void s() {
        System.out.println(Arrays.toString(Number.toHH(5)));
    }
}