package Disk;

import java.io.IOException;

class DiskTest {
    @org.junit.jupiter.api.Test
    void mkfs() throws IOException {
//        Disk disk = new Disk("image", 5 * SizeType.G);
        Disk disk = new Disk("image");
        System.out.println(disk.getName());
        System.out.println(disk.getSize());
    }
}