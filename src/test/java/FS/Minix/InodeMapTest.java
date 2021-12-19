package FS.Minix;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class InodeMapTest {
    @Test
    void toBytes() throws IOException {
        InodeMap inodeMap = new InodeMap();
        inodeMap.setSize(12288 * 4096);
        System.out.println(inodeMap.toBytes().length);
    }
}