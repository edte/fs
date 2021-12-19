package runtime;

import Disk.Disk;
import FS.Minix.Minix;
import VFS.Dentry;
import VFS.File;
import VFS.Inode;
import VFS.SuperBlock;

import java.io.IOException;

public class runtime {
    public static Disk disk;

    public static VFS.SuperBlock superBlock;

    public static VFS.Dentry dentry;

    public static VFS.File file;

    public static VFS.Inode inode;

    public static Disk getDisk() {
        return disk;
    }

    public static void setDisk(Disk disk) {
        runtime.disk = disk;
    }

    public static SuperBlock getSuperBlock() {
        return superBlock;
    }

    public static void setSuperBlock(SuperBlock superBlock) {
        runtime.superBlock = superBlock;
    }

    public static Dentry getDentry() {
        return dentry;
    }

    public static void setDentry(Dentry dentry) {
        runtime.dentry = dentry;
    }

    public static File getFile() {
        return file;
    }

    public static void setFile(File file) {
        runtime.file = file;
    }

    public static Inode getInode() {
        return inode;
    }

    public static void setInode(Inode inode) {
        runtime.inode = inode;
    }

    public static void init() throws IOException {
        runtime.setDisk(new Disk("image"));

        runtime.setSuperBlock(new SuperBlock());
        runtime.getSuperBlock().fs = new Minix();
        Dentry d = new Dentry();
        d.setName("/");
        d.setIno(0);
        runtime.getSuperBlock().root = d;

        Dentry dentry = new Dentry();
        dentry.setName("/");
        dentry.setIno(0);
        runtime.setDentry(dentry);
        runtime.setInode(new Inode());
    }
}
