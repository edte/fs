package FS.Minix;

import utils.Number;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 文件的元信息
 */
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
    // 填充使 inode 占 256 B
    protected byte[] padding = new byte[192];

    public Inode() {
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

    public byte[] getPadding() {
        return padding;
    }

    public void setPadding(byte[] padding) {
        this.padding = padding;
    }

    public Inode(int mode, int ino, int blocksNumber, int[] blockList, int fileSize, int uid, int createTime, int accessTime, int linkNum) {
        this.mode = mode;
        this.ino = ino;
        this.blocksNumber = blocksNumber;
        this.blockList = blockList;
        this.fileSize = fileSize;
        this.uid = uid;
        this.createTime = createTime;
        this.accessTime = accessTime;
        this.linkNum = linkNum;
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(Number.toHH(mode));
        stream.write(Number.toHH(ino));
        stream.write(Number.toHH(blocksNumber));
        stream.write(Number.itob(blockList));
        stream.write(Number.toHH(fileSize));
        stream.write(Number.toHH(uid));
        stream.write(Number.toHH(createTime));
        stream.write(Number.toHH(accessTime));
        stream.write(Number.toHH(linkNum));
        stream.write(padding);
        return stream.toByteArray();
    }

    static Inode parse(byte[] bytes) {
        Inode inode = new Inode();
        inode.setMode(Number.toHI(utils.Number.subBytes(bytes, 0, 4)));
        inode.setIno(Number.toHI(utils.Number.subBytes(bytes, 4, 4)));
        inode.setBlocksNumber(Number.toHI(utils.Number.subBytes(bytes, 8, 4)));
        inode.setBlockList(Number.btoi(utils.Number.subBytes(bytes, 12, 32)));
        inode.setFileSize(Number.toHI(utils.Number.subBytes(bytes, 44, 4)));
        inode.setUid(Number.toHI(utils.Number.subBytes(bytes, 48, 4)));
        inode.setCreateTime(Number.toHI(utils.Number.subBytes(bytes, 52, 4)));
        inode.setAccessTime(Number.toHI(utils.Number.subBytes(bytes, 56, 4)));
        inode.setLinkNum(Number.toHI(utils.Number.subBytes(bytes, 60, 4)));
        return inode;
    }
}
