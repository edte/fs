package FS;

import Disk.Disk;
import FS.Minix.*;

import java.io.IOException;

public interface FS {
    void mkfs() throws IOException;

    void setDisk(Disk disk);

    void mount(String name) throws IOException;

    Inode readInode(int inodeNum) throws IOException;

    Dentry readDentry(int ino) throws IOException;

    int createDir(String name, int pino) throws IOException;

    int createFile(String name, byte[] bytes, int pino) throws IOException;

    byte[] readFile(int ino) throws IOException;

    SuperBlock getSuperBlock();

    InodeMap getImap();

    BlockMap getBmap();

    void Delete(int ino, int pino) throws IOException;
}