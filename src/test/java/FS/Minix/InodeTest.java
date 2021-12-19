package FS.Minix;

import java.io.IOException;

class InodeTest {
    @org.junit.jupiter.api.Test
    void toBytes() throws IOException {
        Inode inode = new Inode(0, 0, 0, new int[8], 2, 3, 5, 6, 1);
        System.out.println(inode.toBytes().length);
    }
}