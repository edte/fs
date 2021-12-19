package FS.Minix;

import org.junit.jupiter.api.Test;

import java.io.IOException;

class SuperBlockTest {
    @Test
    void toBytes() throws IOException {
        SuperBlock superBlock = new SuperBlock();
        System.out.println(superBlock.toBytes().length);
    }
}