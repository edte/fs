package FS.Minix;

import utils.Number;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

// 一个 Dentry 用一个 block 来存,一个 block 只存一个存一个文件
public class Dentry {
    // 名字长度
    int nameLength;
    // 目录名
    String name;
    // 父目录的 inodeNum
    int pino;
    // 当前目录的 inodeNum
    int ino;
    // 当前目录下的文件或目录数
    int dirNum;
    // 子 dir
    Dir[] dirs;

    byte[] bytes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPino() {
        return pino;
    }

    public void setPino(int pino) {
        this.pino = pino;
    }

    public int getIno() {
        return ino;
    }

    public void setIno(int ino) {
        this.ino = ino;
    }

    public int getDirNum() {
        return dirNum;
    }

    // 默认两个，即当前和父目录文件
    public void setDirNum(int dirNum) {
        this.dirNum = dirNum;
    }

    public Dir[] getDirs() {
        return dirs;
    }

    public void setDirs(Dir[] dirs) {
        this.dirs = dirs;
    }

    public void setNameLength(int nameLength) {
        this.nameLength = nameLength;
    }

    public int getNameLength() {
        return nameLength;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(Number.toHH(nameLength));
        stream.write(name.getBytes());
        stream.write(Number.toHH(pino));
        stream.write(Number.toHH(ino));
        stream.write(Number.toHH(dirNum));
        for (Dir dir : dirs) {
            stream.write(dir.toBytes());
        }
        setBytes(stream.toByteArray());
        return stream.toByteArray();
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    static public Dentry parse(byte[] bytes) {
        Dentry dentry = new Dentry();
        dentry.setNameLength(Number.toHI(utils.Number.subBytes(bytes, 0, 4)));
        dentry.setName(new String(bytes, 4, dentry.nameLength));
        dentry.setPino(Number.toHI(utils.Number.subBytes(bytes, 4 + dentry.nameLength, 4)));
        dentry.setIno(Number.toHI(utils.Number.subBytes(bytes, 8 + dentry.nameLength, 4)));
        dentry.setDirNum(Number.toHI(utils.Number.subBytes(bytes, 12 + dentry.nameLength, 4)));

        Dir[] dirs = new Dir[dentry.dirNum];

        byte[] bytes1 = Number.subBytes(bytes, 16 + dentry.nameLength, 263 * dentry.dirNum);
        for (int i = 0; i < dentry.dirNum; i++) {
            dirs[i] = Dir.parse(Number.subBytes(bytes1, 263 * i, 263));
        }
        dentry.setDirs(dirs);
        return dentry;
    }
}
