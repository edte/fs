package VFS;

import java.io.IOException;

public class Inode {
    // 文件类型，0 表示文件，1 表示目录
    protected int mode;
    // inode 编号
    protected int ino;
    // 文件占用的 block 数目
    protected int blocksNumber;
    // 文件所在的 block 索引
    protected int[] blockList;
    // 文件大小
    protected int fileSize;
    // 用户 id
    protected int uid;
    // 创建时间,存 unix 时间戳
    protected int createTime;
    // 访问时间
    protected int accessTime;
    // link 到该 inode 的文件数
    protected int linkNum;

    Inode getInode(int ino) throws IOException {
        return runtime.runtime.getSuperBlock().getInode(ino);
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getIno() {
        return ino;
    }

    public void setIno(int ino) {
        this.ino = ino;
    }

    public int getBlocksNumber() {
        return blocksNumber;
    }

    public void setBlocksNumber(int blocksNumber) {
        this.blocksNumber = blocksNumber;
    }

    public int[] getBlockList() {
        return blockList;
    }

    public void setBlockList(int[] blockList) {
        this.blockList = blockList;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(int accessTime) {
        this.accessTime = accessTime;
    }

    public int getLinkNum() {
        return linkNum;
    }

    public void setLinkNum(int linkNum) {
        this.linkNum = linkNum;
    }
}
