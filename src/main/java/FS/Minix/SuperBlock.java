package FS.Minix;

import utils.Number;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 超级块，存放相关文件系统相关的信息
 * 注意，首地址是指相关区域的第一个块的块数
 * 显然，boot 块应该是第一块
 * super block 块是第二块
 */
public class SuperBlock {
    // 文件系统类型
    protected int magic;
    // 块大小
    protected int blockSize;
    // inode 大小
    protected int inodeSize;
    // 总块数
    protected int blocksCount;
    // 空闲块数
    protected int freeBlocksCount;
    // inode 节点总数
    protected int inodesCount;

    // imap 需要的块数
    protected int imapCount;
    // bmap 需要的块数
    protected int bmapCount;

    // inodeMap 区的首地址
    protected int imapPointer;
    // blockMap 区的首地址
    protected int bmapPointer;
    // inode 数据区首地址
    protected int inodeTablePointer;
    // block 数据区首地址
    protected int dataBlocksPointer;
    // 填充，使超级块占 4096B
    protected byte[] padding = new byte[4048];

    // 序列化后的数据长度
    public static int toSizeLength;

    static {
        try {
            toSizeLength = new SuperBlock().toBytes().length - 4048;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public void setInodesCount(int inodesCount) {
        this.inodesCount = inodesCount;
    }

    public void setFreeBlocksCount(int freeBlocksCount) {
        this.freeBlocksCount = freeBlocksCount;
    }

    public void setBlocksCount(int blocksCount) {
        this.blocksCount = blocksCount;
    }

    public void setInodeSize(int inodeSize) {
        this.inodeSize = inodeSize;
    }

    public void setImapCount(int imapCount) {
        this.imapCount = imapCount;
    }

    public void setBmapCount(int bmapCount) {
        this.bmapCount = bmapCount;
    }

    public void setImapPointer(int imapPointer) {
        this.imapPointer = imapPointer;
    }

    public void setBmapPointer(int bmapPointer) {
        this.bmapPointer = bmapPointer;
    }

    public void setInodeTablePointer(int inodeTablePointer) {
        this.inodeTablePointer = inodeTablePointer;
    }

    public void setDataBlocksPointer(int dataBlocksPointer) {
        this.dataBlocksPointer = dataBlocksPointer;
    }

    public int getMagic() {
        return magic;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getInodeSize() {
        return inodeSize;
    }

    public int getBlocksCount() {
        return blocksCount;
    }

    public int getFreeBlocksCount() {
        return freeBlocksCount;
    }

    public int getInodesCount() {
        return inodesCount;
    }

    public int getImapCount() {
        return imapCount;
    }

    public int getBmapCount() {
        return bmapCount;
    }

    public int getImapPointer() {
        return imapPointer;
    }

    public int getBmapPointer() {
        return bmapPointer;
    }

    public int getInodeTablePointer() {
        return inodeTablePointer;
    }

    public int getDataBlocksPointer() {
        return dataBlocksPointer;
    }

    // superblock 占据一个块，即 4096B
    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(Number.toHH(magic));
        stream.write(Number.toHH(blockSize));
        stream.write(Number.toHH(inodeSize));
        stream.write(Number.toHH(inodesCount));
        stream.write(Number.toHH(imapCount));
        stream.write(Number.toHH(bmapCount));
        stream.write(Number.toHH(freeBlocksCount));
        stream.write(Number.toHH(blocksCount));
        stream.write(Number.toHH(imapPointer));
        stream.write(Number.toHH(bmapPointer));
        stream.write(Number.toHH(inodeTablePointer));
        stream.write(Number.toHH(dataBlocksPointer));
        stream.write(padding);
        return stream.toByteArray();
    }

    static SuperBlock parse(byte[] bytes) {
        SuperBlock superBlock = new SuperBlock();

        superBlock.setMagic(utils.Number.toHI(utils.Number.subBytes(bytes, 0, 4)));
        superBlock.setBlockSize(utils.Number.toHI(utils.Number.subBytes(bytes, 4, 4)));
        superBlock.setInodeSize(utils.Number.toHI(utils.Number.subBytes(bytes, 8, 4)));
        superBlock.setInodesCount(utils.Number.toHI(utils.Number.subBytes(bytes, 12, 4)));
        superBlock.setImapCount(utils.Number.toHI(utils.Number.subBytes(bytes, 16, 4)));
        superBlock.setBmapCount(utils.Number.toHI(utils.Number.subBytes(bytes, 20, 4)));
        superBlock.setFreeBlocksCount(utils.Number.toHI(utils.Number.subBytes(bytes, 24, 4)));
        superBlock.setBlocksCount(utils.Number.toHI(utils.Number.subBytes(bytes, 28, 4)));
        superBlock.setImapPointer(utils.Number.toHI(utils.Number.subBytes(bytes, 32, 4)));
        superBlock.setBmapPointer(utils.Number.toHI(utils.Number.subBytes(bytes, 36, 4)));
        superBlock.setInodeTablePointer(utils.Number.toHI(utils.Number.subBytes(bytes, 40, 4)));
        superBlock.setDataBlocksPointer(utils.Number.toHI(utils.Number.subBytes(bytes, 44, 4)));

        return superBlock;
    }
}
