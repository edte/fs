package FS.Minix;

import org.junit.jupiter.api.Test;
import utils.Number;

import java.io.IOException;

class BlockMapTest {
    @Test
    void toBytes() throws IOException {
        BlockMap map = new BlockMap();
        map.setSize(163840);
        map.setUsedSize(48);
        byte[] bytes = map.toBytes();
        System.out.println(Number.baseConversion(bytes, 2));
    }
}