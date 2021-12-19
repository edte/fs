package FS.Minix;

import Disk.Disk;
import Disk.SizeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

class MinixTest {
    @Test
    void writeStream() throws IOException {
        Minix minix = new Minix();
        minix.setDisk(new Disk("image", 5 * SizeType.G));
//        minix.mkfs();

//        System.out.println(minix.readBoot().getData());
//        System.out.println(minix.readSuperBlock().getInodeSize());
//        System.out.println(minix.readSuperBlock().getBmapPointer());

//        InodeMap inodeMap = minix.readImap(3, 6);
//        System.out.println(Arrays.toString(inodeMap.getMaps()));

//        int freeInode = minix.findFreeInode(3, 6);
//        System.out.println(freeInode);
//        System.out.println(minix.inoToBno(freeInode));

//        int freeBnode = minix.findFreeBnode();
//        System.out.println(freeBnode);

//        BlockMap blockMap = minix.readBmap(9, 40);
//        System.out.println(utils.Number.baseConversion(blockMap.getMaps(), 2));

//        byte[] bytes = "我爱你".getBytes();
//        minix.writeBlock(bytes, 9999);
//        Block block = minix.readBlock(9999);
//        System.out.println(new String(block.getBytes()));
    }

    @Test
    void createDirTest() throws IOException {
        Minix minix = new Minix();
        minix.mkfs();
        minix.mount("rootfs");
        Dentry dentry = minix.readDentry(0);
        for (Dir dir : dentry.getDirs()) {
            System.out.println(dir.filename);
        }
    }

    @Test
    void CreatFileTest() throws IOException {
        Minix minix = new Minix();
        minix.mkfs();
        minix.mount("rootfs");

        byte[] bytes1 = "这是一个测试数据，hhhhhhhhhhhhhhhhhhhhhhhh".getBytes();
        int file = minix.createFile("a.txt", bytes1, 7);

        Dentry dentry = minix.readDentry(7);
        byte[] bytes = minix.readFile(file);
        System.out.println(new String(bytes));
    }

    @Test
    void DeleteFile() throws IOException {
        Minix minix = new Minix();
        minix.mkfs();
        minix.mount("rootfs");
        System.out.println(utils.Number.baseConversion(minix.getImap().getMaps(), 2));
//        Dentry dentry = minix.readDentry(0);
//        minix.Delete(2, 0);
//        minix.Delete(3, 0);
//        minix.Delete(5, 0);
//        System.out.println(utils.Number.baseConversion(minix.getBmap().getMaps(), 2));
//        Dentry dentry1 = minix.readDentry(0);

    }
}