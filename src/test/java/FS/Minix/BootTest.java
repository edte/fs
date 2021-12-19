package FS.Minix;

import java.io.IOException;

class BootTest {
    @org.junit.jupiter.api.Test
    void toBytes() throws IOException {
        Boot boot = new Boot();
        System.out.println(boot.toBytes().length);
    }
}