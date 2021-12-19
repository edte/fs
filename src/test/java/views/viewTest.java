package views;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class viewTest {

    @Test
    void main() {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(String.valueOf((int) new Date().getTime())))));

    }
}