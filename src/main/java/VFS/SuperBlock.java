package VFS;

import FS.Minix.Minix;
import runtime.runtime;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class SuperBlock extends Inode {
    // 文件系统
    public FS.FS fs;
    // 具体文件系统超级块链表
    LinkedList<FS.Minix.SuperBlock> list;
    // 文件系统类型
    FS.FSType type;
    // 文件系统魔数
    int magic;
    // 文件的最长长度
    int FileSize;
    // 根目录
    public Dentry root;
    // imap
    byte[] imap;
    // bmap
    byte[] bmap;

    public void mkfs(String type) throws IOException {
        if (type.equals("minix")) {
            fs = new Minix();
            fs.setDisk(runtime.disk);
            fs.mkfs();
        } else {
            throw new IOException("unknown fs type!");
        }
    }

    public void mount(String type) throws IOException {
        if (type.equals("rootfs")) {
            fs.mount(type);
        } else {
            throw new IOException("unknown mount type!");
        }
    }

    Inode getInode(int ino) throws IOException {
        Inode inode = new Inode();
        FS.Minix.Inode inode1 = fs.readInode(ino);
        inode.setIno(inode1.getIno());
        inode.setUid(inode1.getUid());
        inode.setLinkNum(inode1.getLinkNum());
        inode.setAccessTime(inode1.getAccessTime());
        inode.setCreateTime(inode1.getCreateTime());
        inode.setFileSize(inode1.getFileSize());
        inode.setMode(inode1.getMode());
        return inode;
    }

    FS.Minix.Dentry readDentry(int ino) throws IOException {
        return fs.readDentry(ino);
    }

    int createDir(String name, int pino) throws IOException {
        return fs.createDir(name, pino);
    }

    int createFile(String name, String data, int pino) throws IOException {
        return fs.createFile(name, data.getBytes(), pino);
    }

    byte[] readInodeData(int inodeNum) throws IOException {
        return fs.readFile(inodeNum);
    }

    public void superblock() {
        FS.Minix.SuperBlock superBlock = fs.getSuperBlock();
        System.out.println("Magic:  " + superBlock.getMagic());
        System.out.println("BlockSize:  " + superBlock.getBlockSize());
        System.out.println("InodeSize:  " + superBlock.getInodeSize());
        System.out.println("BlocksCount:  " + superBlock.getBlocksCount());
        System.out.println("InodesCount:  " + superBlock.getInodesCount());
        System.out.println("ImapPointer:  " + superBlock.getImapPointer());
        System.out.println("BmapPointer:  " + superBlock.getBmapPointer());
        System.out.println("InodeTablePointer:  " + superBlock.getInodeTablePointer());
        System.out.println("DataBlocksPointer:  " + superBlock.getDataBlocksPointer());
    }

    public void imap() {
        System.out.println(Arrays.toString(fs.getImap().getMaps()));
    }

    public void bmap() {
        System.out.println(Arrays.toString(fs.getBmap().getMaps()));
    }

    public void Delete(int ino, int pino) throws IOException {
        fs.Delete(ino, pino);
    }
}
