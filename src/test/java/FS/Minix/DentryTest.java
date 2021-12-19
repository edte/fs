package FS.Minix;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

class DentryTest {
    @Test
    void i() throws IOException {
        Dentry dentry = new Dentry();
        dentry.setNameLength("/".getBytes().length);
        dentry.setName("/");
        dentry.setPino(0);
        dentry.setIno(0);
        dentry.setDirNum(1);
        dentry.setDirs(new Dir[]{new Dir("a.txt", "a.txt".getBytes().length, 1)});
        System.out.println(Arrays.toString(dentry.toBytes()));
    }
}